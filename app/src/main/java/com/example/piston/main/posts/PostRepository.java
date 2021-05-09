package com.example.piston.main.posts;

import android.util.Log;

import com.example.piston.data.NotificationReply;
import com.example.piston.data.Post;
import com.example.piston.data.Reply;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PostRepository {

    private final PostRepository.IPosts listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user;
    private final String collection;
    private final String document;
    private final String postID;
    private final DocumentReference docRef;
    private ListenerRegistration listenerRegistration;

    public interface IPosts {
        void setReplies(ArrayList<Reply> replies);
        void setPostTitle(String title);
        void setPost(Post post);
        void setIsLiked(boolean liked);
    }

    public PostRepository(PostRepository.IPosts listener, String collection, String document, String postID) {
        this.listener = listener;
        this.collection = collection;
        this.document = collection;
        this.postID = postID;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        db.collection("users")
                .document(Objects.requireNonNull(email))
                .get()
                .addOnCompleteListener(task ->
                        user = (String) Objects.requireNonNull(task.getResult()).get("username"));

        if (collection.equals("folders")) {
            docRef = db.collection("users")
                    .document(email)
                    .collection(collection)
                    .document(document)
                    .collection("posts")
                    .document(postID);
        }
        else {
            docRef = db.collection(collection)
                    .document(document)
                    .collection("posts")
                    .document(postID);
        }

        docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.setPostTitle((String) Objects.requireNonNull(task.getResult()).get("title"));
                    }
        });
        listenChanges();
    }

    //Añadir como atributo al hacer reply
    //timestamp: FieldValue.serverTimestamp()
    private void loadPosts() {
        ArrayList<Reply> posts = new ArrayList<>();

        docRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               listener.setPost((Objects.requireNonNull(task.getResult()).toObject(Post.class)));
               docRef.collection("replies")
                       .orderBy("timestamp")
                       .get().addOnCompleteListener(task1 -> {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                               task1.getResult())) {
                           Reply post = documentSnapshot.toObject(Reply.class);
                           posts.add(post);
                       }
                   } else {
                       Log.d("nowaybro", "Error getting documents: ", task.getException());
                   }
                   listener.setReplies(posts);
               });
           }
        });
    }

    private void listenChanges() {
        listenerRegistration = docRef.collection("replies")
                    .addSnapshotListener((snapshots, e) -> PostRepository.this.loadPosts());
    }

    public void createReply(String content) {
        String id = db.collection("users").document().getId();
        Reply rep = new Reply(user, content, id);
        DocumentReference docRef1 = docRef.collection("replies")
                                        .document(id);
        Map<String, Object> value = new HashMap<>();

        value.put("timestamp", FieldValue.serverTimestamp());

        docRef1.set(rep);
        docRef1.update(value);

        value.put("type", "reply");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                db.collection("emails").document(Objects.requireNonNull(Objects
                        .requireNonNull(task.getResult()).get("owner")).toString())
                        .get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && !task1.getResult().getId().equals(user)) {
                                NotificationReply notificationReply = new NotificationReply(user, content,
                                        false, collection, document, postID);
                                DocumentReference docRef2 = db.collection("users")
                                        .document(Objects.requireNonNull(Objects.requireNonNull
                                                (task1.getResult()).get("email")).toString())
                                        .collection("notifications")
                                        .document();
                                docRef2.set(notificationReply);
                                docRef2.update(value);
                            }
                });
            }
        });
    }

    public void createReply(String content, String quote, String quoteOwner, String quoteID) {
        Log.d("DBReadTAG", content + " " + quote + " " + quoteOwner);
        String id = db.collection("users").document().getId();
        Reply rep = new Reply(user, content, id, quote, quoteOwner, quoteID);
        DocumentReference docRef1 = docRef.collection("replies")
                .document(id);
        Map<String, Object> value = new HashMap<>();

        value.put("timestamp", FieldValue.serverTimestamp());

        docRef1.set(rep);
        docRef1.update(value);

        value.put("type", "reply");

        if (!quoteOwner.equals(user)) {
            db.collection("emails").document(quoteOwner).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    NotificationReply notificationReply = new NotificationReply(user, content, false,
                            collection, document, postID);
                    DocumentReference docRef2 = db.collection("users")
                            .document(Objects.requireNonNull(Objects.requireNonNull(task.getResult())
                                    .get("email")).toString())
                            .collection("notifications")
                            .document();
                    docRef2.set(notificationReply);
                    docRef2.update(value);
                }
            });
        }
    }

    public void addLiked (boolean liked, String postID){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        if (liked){
            Map <String, String> data = new HashMap<>();
            data.put("id", postID);
            db.collection("users").document(email).collection("liked").document(postID).set(data);
        }
        else{
            db.collection("users").document(email).collection("liked").document(postID).delete();
        }

    }

    public void checkLiked (String postID){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        DocumentReference dR = db.collection("users").document(email).collection("liked").document(postID);
        dR.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot ds = task.getResult();
                listener.setIsLiked(ds.exists());
            }
        });


    }

    public void updateNumLikes(boolean liked){
       docRef.get().addOnSuccessListener(documentSnapshot -> {
           Post post = documentSnapshot.toObject(Post.class);
           int likes;
           if (liked){
               likes = post.getNumLikes()+1;
               docRef.update("numLikes", likes);
           }
           else{
               likes = post.getNumLikes()-1;
               docRef.update("numLikes", likes);
           }
       });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

    }
