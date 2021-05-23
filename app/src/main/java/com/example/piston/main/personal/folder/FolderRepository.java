package com.example.piston.main.personal.folder;

import com.example.piston.data.posts.Post;
import com.example.piston.utilities.Values;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

public class FolderRepository {

    public interface IFolder {
        void setFolderPosts(ArrayList<Post> posts);
        void setTitle(String title);
        void setFilter(String filter);
    }

    private final FolderRepository.IFolder listener;
    private ListenerRegistration listenerRegistration;
    private ListenerRegistration listenerRegistrationFolder;
    private final DocumentReference folderDocRef;
    private Query postsQuery;

    public FolderRepository(FolderRepository.IFolder listener, String folder) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        folderDocRef = db.collection("users")
                .document(user)
                .collection("folders")
                .document(folder);

        postsQuery = folderDocRef.collection("posts").orderBy("pinned", Query.Direction.DESCENDING)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        updateParams();
        listenChanges();
    }

    private void loadFolderPosts() {
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
                listener.setFolderPosts(posts);
            }
        });
    }

    public void updateQuery(String field, boolean descending) {
        if (field.equals(Values.FILTER_DEFAULT))
            postsQuery = folderDocRef.collection("posts").orderBy("pinned",
                    Query.Direction.DESCENDING).orderBy("timestamp", Query.Direction.DESCENDING);
        else
            postsQuery = folderDocRef.collection("posts").orderBy("pinned",
                    Query.Direction.DESCENDING).orderBy(field, (descending) ?
                    Query.Direction.DESCENDING : Query.Direction.ASCENDING)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
        loadFolderPosts();
        listener.setFilter(field);
    }

    public void updateParams() {
        folderDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setTitle(Objects.requireNonNull(task.getResult().get("title")).toString());
            }
        });
    }

    private void listenChanges() {
        listenerRegistration = postsQuery.addSnapshotListener((snapshots, e) -> FolderRepository
                .this.loadFolderPosts());
        listenerRegistrationFolder = folderDocRef.addSnapshotListener((value, error) ->
                FolderRepository.this.updateParams());
    }

    public void removeListener() {
        listenerRegistrationFolder.remove();
        listenerRegistration.remove();
    }
}
