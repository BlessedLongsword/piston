package com.example.piston.main.personal.folder;

import android.util.Log;

import com.example.piston.data.Post;
import com.example.piston.main.personal.PersonalRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FolderRepository {

    public interface IFolder {
        void setFolderPosts(ArrayList<Post> posts);
    }

    private final FolderRepository.IFolder listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String user, folder;
    private ListenerRegistration listenerRegistration;

    public FolderRepository(FolderRepository.IFolder listener, String folder) {
        this.listener = listener;
        this.user = auth.getCurrentUser().getEmail();
        this.folder = folder;
        listenChanges();
    }

    public void loadFolderPosts() {
        db.collection("users")
                .document(user)
                .collection("folders")
                .document(folder)
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
                        listener.setFolderPosts(posts);
                    } else {
                        Log.d("nowaybro", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void listenChanges() {
        listenerRegistration = db.collection("users")
                .document(user)
                .collection("folders")
                .document(folder)
                .collection("posts")
                .addSnapshotListener((snapshots, e) -> {
                    FolderRepository.this.loadFolderPosts();
                });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
