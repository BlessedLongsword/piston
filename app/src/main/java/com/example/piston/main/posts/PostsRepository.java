package com.example.piston.main.posts;

import android.util.Log;

import com.example.piston.data.Folder;
import com.example.piston.data.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class PostsRepository {

    private final PostsRepository.IPosts listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String user, collection, document, postID;
    private ListenerRegistration listenerRegistration;

    public interface IPosts {
        void setPosts(ArrayList<Post> categories);
    }

    public PostsRepository(PostsRepository.IPosts listener, String collection, String document, String postID) {
        this.listener = listener;
        this.collection = collection;
        this.document = document;
        this.postID = postID;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        listenChanges();
    }

    private void loadPosts() {
        db.collection(collection)
                .document(document)
                .collection("posts")
                .document(postID)
                .collection("replies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Post> posts = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            Post post = documentSnapshot.toObject(Post.class);
                            posts.add(post);
                        }
                        listener.setPosts(posts);
                    } else {
                        Log.d("nowaybro", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void listenChanges() {
        listenerRegistration = db.collection("users")
                .document(user)
                .collection("posts")
                .addSnapshotListener((snapshots, e) -> PostsRepository.this.loadPosts());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
