package com.example.piston.main.posts;

import android.util.Log;

import com.example.piston.data.Post;
import com.example.piston.data.Reply;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostRepository {

    private final PostRepository.IPosts listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  String user, email, collection, document, postID;
    private final DocumentReference docRef;
    private ListenerRegistration listenerRegistration;

    public interface IPosts {
        void setReplies(ArrayList<Reply> replies);
        void setPostTitle(String title);
        void setPost(Post post);
    }

    public PostRepository(PostRepository.IPosts listener, String collection, String document, String postID) {
        this.listener = listener;
        this.collection = collection;
        this.document = document;
        this.postID = postID;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        db.collection("users")
                .document(email)
                .get()
                .addOnCompleteListener(task ->
                        user = (String) task.getResult().get("username"));

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
                        listener.setPostTitle((String) task.getResult().get("title"));
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
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
