package com.example.piston.main.global.category;

import com.example.piston.data.posts.Post;
import com.example.piston.utilities.Values;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

public class CategoryRepository {

    public interface ICategory {
        void setCategoryPosts(ArrayList<Post> posts);
        void setTitle(String title);
        void setFilter(String filter);
    }

    private final ICategory listener;
    private Query postsQuery;
    private ListenerRegistration listenerRegistration;
    private ListenerRegistration listenerRegistrationCategory;
    private final DocumentReference categoryDocRef;

    public CategoryRepository(ICategory listener, String category) {
        this.listener = listener;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        categoryDocRef = db.collection("categories").document(category);

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
                            Timestamp timestamp = (Timestamp) documentSnapshot.get("timestamp", ESTIMATE);
                            post.setTimeAgo(Objects.requireNonNull(timestamp).getSeconds());
                            posts.add(post);
                        }
                        listener.setCategoryPosts(posts);
                    }
                });
    }

    public void updateQuery(String field, boolean descending) {
        if (field.equals(Values.FILTER_DEFAULT))
            postsQuery = categoryDocRef.collection("posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING);
        else
            postsQuery = categoryDocRef.collection("posts")
                    .orderBy(field, (descending) ? Query.Direction.DESCENDING
                            : Query.Direction.ASCENDING).orderBy("timestamp",
                    Query.Direction.DESCENDING);
        loadCategoryPosts();
        listener.setFilter(field);
    }

    private void listenChanges() {
        listenerRegistration = postsQuery.addSnapshotListener((snapshots, e) -> CategoryRepository
                .this.loadCategoryPosts());
        listenerRegistrationCategory = categoryDocRef.addSnapshotListener((value, error) ->
                CategoryRepository.this.updateParams());
    }

    private void updateParams() {
        categoryDocRef.get().addOnCompleteListener(task -> {
            if (task.isComplete() && task.getResult().exists())
                listener.setTitle((String) task.getResult().get("title"));
        });
    }
    public void removeListener() {
        listenerRegistrationCategory.remove();
        listenerRegistration.remove();
    }

}
