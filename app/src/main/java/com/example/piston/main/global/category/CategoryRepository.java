package com.example.piston.main.global.category;

import android.util.Log;

import com.example.piston.data.Post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryRepository {

    public interface ICategory {
        void setCategoryPosts(ArrayList<Post> posts);
    }

    private final ICategory listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String category;
    private ListenerRegistration listenerRegistration;

    public CategoryRepository(ICategory listener, String category) {
        this.listener = listener;
        this.category = category;
        listenChanges();
    }

    private void loadCategoryPosts() {
        db.collection("categories")
                .document(category)
                .collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Post> posts = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            Post post = documentSnapshot.toObject(Post.class);
                            posts.add(post);
                        }
                        listener.setCategoryPosts(posts);
                    } else {
                        Log.d("nowaybro", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void listenChanges() {
        listenerRegistration = db.collection("categories")
                .document(category)
                .collection("posts")
                .addSnapshotListener((snapshots, e) -> CategoryRepository.this.loadCategoryPosts());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
