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
        void setTitle(String title);
    }

    private final GroupRepository.IGroup listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String group;
    private ListenerRegistration listenerRegistration;

    public GroupRepository(GroupRepository.IGroup listener, String group) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        this.group = group;
        db.collection("groups")
                .document(group)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.setTitle(Objects.requireNonNull(task.getResult().get("title")).toString());
                    }
                });
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
                .addSnapshotListener((snapshots, e) -> GroupRepository.this.loadGroupPosts());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}