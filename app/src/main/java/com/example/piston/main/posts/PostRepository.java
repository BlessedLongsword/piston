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
import com.google.firestore.v1.DocumentTransform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostRepository {

    private final PostRepository.IPosts listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String user, collection, document, postID;
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
        user = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        db.collection(collection)
                .document(document)
                .collection("posts")
                .document(postID).get().addOnCompleteListener(task -> {
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
        DocumentReference docRef = db.collection(collection)
            .document(document)
            .collection("posts")
            .document(postID);

        docRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               listener.setPost((Objects.requireNonNull(task.getResult()).toObject(Post.class)));
               docRef.collection("replies")
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
        listenerRegistration = db.collection(collection)
                .document(document)
                .collection("posts")
                .document(postID)
                .collection("replies")
                .addSnapshotListener((snapshots, e) -> PostRepository.this.loadPosts());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
