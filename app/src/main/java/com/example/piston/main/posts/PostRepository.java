package com.example.piston.main.posts;

import android.util.Log;
import android.widget.PopupWindow;

import com.example.piston.data.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class PostRepository {

    private final PostRepository.IPosts listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String user, collection, document, postID;
    private ListenerRegistration listenerRegistration;

    public interface IPosts {
        void setPosts(ArrayList<Post> categories);
        void setPostTitle(String title);
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

    private void loadPosts() {
        Log.d("DBReadTAG", "pude entrar xd");
        ArrayList<Post> posts = new ArrayList<>();
        DocumentReference docRef = db.collection(collection)
            .document(document)
            .collection("posts")
            .document(postID);
        docRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               posts.add(Objects.requireNonNull(task.getResult()).toObject(Post.class));
               docRef.collection("replies").get().addOnCompleteListener(task1 -> {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                               task1.getResult())) {
                           Post post = documentSnapshot.toObject(Post.class);
                           posts.add(post);
                       }
                   } else {
                       Log.d("nowaybro", "Error getting documents: ", task.getException());
                   }
                   listener.setPosts(posts);
               });
           }
        });
    }

    private void listenChanges() {
        listenerRegistration = db.collection(collection)
                .document(document)
                .collection("posts")
                .document(postID)
                .addSnapshotListener((snapshots, e) -> PostRepository.this.loadPosts());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
