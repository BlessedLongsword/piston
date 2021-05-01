package com.example.piston.main.groups.group;

import android.util.Log;

import com.example.piston.data.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class GroupRepository {

    public interface IGroup {
        void setGroupPosts(ArrayList<Post> posts);
    }

    private final GroupRepository.IGroup listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user, group;
    private ListenerRegistration listenerRegistration;

    public GroupRepository(GroupRepository.IGroup listener, String group) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        this.user = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        this.group = group;
        listenChanges();
    }

    private void loadGroupPosts() {
        db.collection("groups")
                .document(group)
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
                        listener.setGroupPosts(posts);
                    } else {
                        Log.d("nowaybro", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void listenChanges() {
        listenerRegistration = db.collection("groups")
                .document(group)
                .collection("posts")
                .addSnapshotListener((snapshots, e) -> {
                    GroupRepository.this.loadGroupPosts();
                });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}