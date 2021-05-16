package com.example.piston.main.global.category;

import com.example.piston.data.posts.Post;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryRepository {

    public interface ICategory {
        void setCategoryPosts(ArrayList<Post> posts);
        void setTitle(String title);
    }

    private final ICategory listener;
    private final DocumentReference categoryDocRef;
    private ListenerRegistration listenerRegistration;

    public CategoryRepository(ICategory listener, String category) {
        this.listener = listener;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        categoryDocRef = db.collection("categories").document(category);
        categoryDocRef.get().addOnCompleteListener(task -> {
            if (task.isComplete())
                listener.setTitle((String) task.getResult().get("title"));
        });

        listenChanges();
    }

    private void loadCategoryPosts() {
        categoryDocRef.collection("posts")
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
                    }
                });
    }

    private void listenChanges() {
        listenerRegistration = categoryDocRef.collection("posts")
                .addSnapshotListener((snapshots, e) -> CategoryRepository.this.loadCategoryPosts());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
