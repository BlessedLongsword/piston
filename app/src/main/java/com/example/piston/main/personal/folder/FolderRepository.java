package com.example.piston.main.personal.folder;

import android.util.Log;

import com.example.piston.data.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FolderRepository {

    public interface IFolder {
        void setFolderPosts(ArrayList<Post> posts);
    }

    private final FolderRepository.IFolder listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String user;

    public FolderRepository(FolderRepository.IFolder listener) {
        this.listener = listener;
        user = auth.getCurrentUser().getEmail();
    }

    public void loadFolderPosts(String folder) {
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
}
