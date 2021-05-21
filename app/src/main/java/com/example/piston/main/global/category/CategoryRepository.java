package com.example.piston.main.global.category;

import com.example.piston.data.posts.Post;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryRepository {

    public interface ICategory {
        void setCategoryPosts(ArrayList<Post> posts);
        void setTitle(String title);
    }

    private final ICategory listener;
    private Query postsQuery;
    private ListenerRegistration listenerRegistration;
    private final DocumentReference categoryDocRef;

    public CategoryRepository(ICategory listener, String category) {
        this.listener = listener;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        categoryDocRef = db.collection("categories").document(category);
        categoryDocRef.get().addOnCompleteListener(task -> {
            if (task.isComplete())
                listener.setTitle((String) task.getResult().get("title"));
        });
        postsQuery = categoryDocRef.collection("posts").orderBy("timestamp",
                Query.Direction.DESCENDING);

        listenChanges();
    }

    private void loadCategoryPosts() {
        postsQuery.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Post> posts = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            Post post = documentSnapshot.toObject(Post.class);
                            Timestamp timestamp = (Timestamp) documentSnapshot.get("timestamp");
                            post.setTimeAgo(Objects.requireNonNull(timestamp).getSeconds());
                            posts.add(post);
                        }
                        listener.setCategoryPosts(posts);
                    }
                });
    }

    public void updateQuery(String field) {
        postsQuery = categoryDocRef.collection("posts").orderBy(field).orderBy("timestamp",
                Query.Direction.DESCENDING);
    }

    private void listenChanges() {
        listenerRegistration = postsQuery.addSnapshotListener((snapshots, e) -> CategoryRepository
                .this.loadCategoryPosts());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
