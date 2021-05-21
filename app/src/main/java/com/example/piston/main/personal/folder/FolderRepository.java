package com.example.piston.main.personal.folder;

import com.example.piston.data.posts.Post;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FolderRepository {

    public interface IFolder {
        void setFolderPosts(ArrayList<Post> posts);
        void setTitle(String title);
    }

    private final FolderRepository.IFolder listener;
    private ListenerRegistration listenerRegistration;
    private final DocumentReference folderDocRef;

    public FolderRepository(FolderRepository.IFolder listener, String folder) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        folderDocRef = db.collection("users")
                .document(user)
                .collection("folders")
                .document(folder);

        folderDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setTitle(Objects.requireNonNull(task.getResult().get("title")).toString());
            }
        });

        listenChanges();
    }

    private void loadFolderPosts() {
        folderDocRef.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Post> posts = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    Post post = documentSnapshot.toObject(Post.class);
                    Timestamp timestamp = (Timestamp) documentSnapshot.get("timestamp");
                    post.setTimeAgo(Objects.requireNonNull(timestamp).getSeconds());
                    posts.add(post);
                }
                listener.setFolderPosts(posts);
            }
        });
    }

    private void listenChanges() {
        listenerRegistration = folderDocRef.collection("posts")
                .addSnapshotListener((snapshots, e) -> FolderRepository.this.loadFolderPosts());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
