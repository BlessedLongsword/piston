package com.example.piston.main.global.category;

import android.util.Log;

import com.example.piston.data.Category;
import com.example.piston.data.Post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryRepository {

    public interface ICategory {
        void setCategoryPosts(ArrayList<Post> posts);
    }

    private final ICategory listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CategoryRepository(ICategory listener) {
        this.listener = listener;
    }

    public void loadCategoryPosts(String category) {
        db.collection("categories")
                .document(category)
                .collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("nowaybro", String.valueOf(task.getResult()));
                        ArrayList<Post> posts = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            Log.d("nowaybro", "Hello====");
                            Post post = documentSnapshot.toObject(Post.class);
                            posts.add(post);
                        }
                        listener.setCategoryPosts(posts);
                    } else {
                        Log.d("nowaybro", "Error getting documents: ", task.getException());
                    }
                });
    }

}
