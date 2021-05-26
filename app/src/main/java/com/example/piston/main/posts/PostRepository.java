package com.example.piston.main.posts;

import com.example.piston.data.notifications.NotificationReply;
import com.example.piston.data.posts.QuoteReply;
import com.example.piston.data.posts.Reply;
import com.example.piston.utilities.Values;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

public class PostRepository {

    private final IPosts listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user;
    private String profilePictureLink;
    private final String email;
    private final String scope, sectionID, postID;
    private final DocumentReference postDocRef;
    private ListenerRegistration listenerRegistration;

    public interface IPosts {
        void setReplies(ArrayList<Reply> replies);
        void setPostParams(String title, String owner, String ownerEmail,
                           String content, String postImageLink, String profileImageLink, String time,
                           Boolean pinned);
        void setLoaded();
        void communicatePinned(boolean pinned);
        void setPriority(Integer priority);
        void setIsLiked(boolean liked);
        void setPostDoesNotExist();
        void setCurrentUser(String currentUser);
        void setNumLikes(String numLikes);
    }

    public PostRepository(IPosts listener, String scope, String sectionID, String postID) {
        this.listener = listener;
        this.scope = scope;
        this.sectionID = sectionID;
        this.postID = postID;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();

        postDocRef = getPostDocRef();

        db.collection("users")
                .document(Objects.requireNonNull(email))
                .get()
                .addOnCompleteListener(task -> {
                    user = (String) task.getResult().get("username");
                    listener.setCurrentUser(user);
                    profilePictureLink = (String) task.getResult().get("profilePictureLink");
                    updateParams();
                    listenChanges();
                });
    }

    private DocumentReference getPostDocRef () {
        DocumentReference base = FirebaseFirestore.getInstance().document("");
        if (scope.equals(Values.PERSONAL))
            base = base.collection("users").document(email);
        return base.collection(scope).document(sectionID).collection("posts")
                .document(postID);
    }

    private void updateParams() {
        postDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DocumentSnapshot ds = task.getResult();
                    String owner = (String) ds.get("owner");
                    Timestamp time = (Timestamp) ds.get("timestamp", ESTIMATE);
                    listener.setPostParams((String) ds.get("title"), owner,
                            (String) ds.get("ownerEmail"), (String) ds.get("content"),
                            (String) ds.get("imageLink"), (String) ds.get("profileImageLink"),
                            TimeAgo.using(Objects.requireNonNull(time).getSeconds()*1000),
                            (Boolean) ds.get("pinned"));

                    long numLikes = (long) Objects.requireNonNull(task.getResult().get("numLikes"));
                    listener.setNumLikes(getLikes(numLikes));

                    if (Objects.requireNonNull(owner).equals(user))
                        listener.setPriority(1);
                } else {
                    listener.setPostDoesNotExist();
                }
            }
        });

        if (scope.equals(Values.GLOBAL)) {
            db.collection("admins")
                    .document(email)
                    .get().addOnCompleteListener(task -> {
                if(task.isSuccessful())
                    listener.setPriority(0);
            });
        }

        if (scope.equals(Values.GROUPS)) {
            db.collection(scope).document(sectionID).collection("members")
                    .document(email).get().addOnCompleteListener(task -> {
               if (task.isSuccessful() &&
                       (long) Objects.requireNonNull(task.getResult().get("priority")) < 2) {
                        listener.setPriority(0);
               }
            });
        }
    }

    private void loadPosts() {
        ArrayList<Reply> posts = new ArrayList<>();
        postDocRef.collection("replies").orderBy("timestamp")
                .get().addOnCompleteListener(task1 -> {
            if (task1.isComplete()) {
                int counter = 0;
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task1.getResult())) {
                   String replyType = Objects.requireNonNull(documentSnapshot.get("type")).toString();
                   Timestamp timestamp = (Timestamp) documentSnapshot.get("timestamp", ESTIMATE);
                   if (replyType.equals("reply")) {
                       Reply post = documentSnapshot.toObject(Reply.class);
                       post.setTimeAgo(Objects.requireNonNull(timestamp).getSeconds());
                       posts.add(post);
                   } else {
                       QuoteReply post = documentSnapshot.toObject(QuoteReply.class);
                       post.setTimeAgo(Objects.requireNonNull(timestamp).getSeconds());
                       posts.add(post);
                   }
                   if (counter++ % 10 == 0)
                       listener.setReplies(posts);
               }
           }
           listener.setReplies(posts);
           listener.setLoaded();
       });
    }

    private void listenChanges() {
        listenerRegistration = postDocRef.collection("replies")
                    .addSnapshotListener((snapshots, e) -> PostRepository.this.loadPosts());
    }

    public void createReply(String content) {
        String id = db.collection("users").document().getId();
        Reply rep = new Reply(id, user, email, content, profilePictureLink);
        DocumentReference docRef1 = postDocRef.collection("replies")
                                        .document(id);
        Map<String, Object> data = new HashMap<>();

        data.put("timestamp", FieldValue.serverTimestamp());
        data.put("type", "reply");

        docRef1.set(rep);
        docRef1.update(data);

        postDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                db.collection("emails").document(Objects.requireNonNull(Objects
                        .requireNonNull(task.getResult()).get("owner")).toString())
                        .get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && !task1.getResult().getId().equals(user)) {
                                DocumentReference docRef2 = db.collection("users")
                                        .document(Objects.requireNonNull(Objects.requireNonNull
                                                (task1.getResult()).get("email")).toString())
                                        .collection("notifications")
                                        .document();
                                String notificationID = docRef2.getId();
                                NotificationReply notificationReply = new NotificationReply(user, content,
                                        id, profilePictureLink, false, scope, sectionID, postID, notificationID);
                                docRef2.set(notificationReply);
                                docRef2.update(data);
                            }
                });
            }
        });
    }

    public void createReply(String content, String quote, String quoteOwner, String quoteID) {
        String id = db.collection("users").document().getId();

        QuoteReply rep = new QuoteReply(id, user, email, content, profilePictureLink, quoteID, quoteOwner, quote);
        DocumentReference docRef1 = postDocRef.collection("replies")
                .document(id);
        Map<String, Object> data = new HashMap<>();

        data.put("timestamp", FieldValue.serverTimestamp());
        data.put("type", "quoteReply");

        docRef1.set(rep);
        docRef1.update(data);

        if (!quoteOwner.equals(user)) {
            db.collection("emails").document(quoteOwner).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentReference docRef2 = db.collection("users")
                            .document(Objects.requireNonNull(Objects.requireNonNull(task.getResult())
                                    .get("email")).toString())
                            .collection("notifications")
                            .document();
                    String notificationID = docRef2.getId();
                    NotificationReply notificationReply = new NotificationReply(user, content, id,
                            profilePictureLink, false, scope, sectionID, postID, notificationID);
                    docRef2.set(notificationReply);
                    docRef2.update(data);
                }
            });
        }
    }

    public void deleteReply(String replyID) {
        postDocRef.collection("replies").document(replyID).delete();
    }

    public void editReply(String replyID, String content) {
        DocumentReference replyDocRef = postDocRef.collection("replies").document(replyID);
        replyDocRef.get().addOnSuccessListener(documentSnapshot -> replyDocRef.update("content", content));
    }

    public void setPinned(boolean pinned) {
        postDocRef.update("pinned", pinned);
        listener.communicatePinned(pinned);
    }

    public void addLiked (boolean liked){
        DocumentReference likedDocRefUsers = db.collection("users")
                .document(Objects.requireNonNull(email)).collection("liked").document(postID);
        DocumentReference likedDocRefPosts = db.collection(scope).document(sectionID)
                .collection("posts").document(postID)
                .collection("userLikes").document(email);
        if (liked) {
            Map <String, String> dataUser = new HashMap<>();
            Map <String, String> dataPost = new HashMap<>();
            dataUser.put("id", postID);
            dataPost.put("email", email);
            likedDocRefUsers.set(dataUser);
            likedDocRefPosts.set(dataPost);
        }
        else {
            likedDocRefUsers.delete();
            likedDocRefPosts.delete();
        }
    }

    public void checkLiked (String postID) {
        DocumentReference dR = db.collection("users")
                .document(Objects.requireNonNull(email)).collection("liked").document(postID);
        dR.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot ds = task.getResult();
                listener.setIsLiked(ds.exists());
            }
        });
    }

    public void updateNumLikes(boolean liked){
       postDocRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               long numLikes = (long) Objects.requireNonNull(task.getResult().get("numLikes"));
               long numLikes1 = liked ? numLikes + 1 : numLikes - 1;
               listener.setNumLikes(getLikes(numLikes1));
               postDocRef.update("numLikes", numLikes1);
           }
       });
    }

    private String getLikes(long numLikes) {
        return (numLikes/1000 > 1) ? numLikes/1000 + "k" : String.valueOf(numLikes);
    }

    public void deletePost() {
        postDocRef.collection("replies")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot1 : Objects.requireNonNull(
                                task.getResult())) {
                            postDocRef.collection("replies")
                                    .document(snapshot1.getId())
                                    .delete();
                        }
                    }
        });
        postDocRef.collection("userLikes")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot1 : Objects.requireNonNull(
                                task.getResult())) {
                            db.collection("users").document(snapshot1.getId())
                                    .collection("liked")
                                    .document(postID)
                                    .delete();

                            postDocRef.collection("userLikes")
                                    .document(snapshot1.getId())
                                    .delete();
                        }
                    }
        });
        StorageReference storageBase;
        if (scope.equals(Values.PERSONAL))
            storageBase = FirebaseStorage.getInstance().getReference().child("users").child(email);
        else
            storageBase = FirebaseStorage.getInstance().getReference().child(scope);
        StorageReference storagePost = storageBase.child(sectionID).child(postID);
        storagePost.delete();

        postDocRef.delete();
    }

    public void updatePost() {
        updateParams();
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
