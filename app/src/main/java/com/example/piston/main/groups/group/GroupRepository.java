package com.example.piston.main.groups.group;

import com.example.piston.data.posts.Post;
import com.example.piston.main.groups.joinGroup.JoinGroupResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GroupRepository {

    public interface IGroup {
        void setGroupPosts(ArrayList<Post> posts);
        void setTitle(String title);
        void setFromShareJoinedGroup();
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
                    }
                });
    }

    private void listenChanges() {
        listenerRegistration = db.collection("groups")
                .document(group)
                .collection("posts")
                .addSnapshotListener((snapshots, e) -> GroupRepository.this.loadGroupPosts());
    }

    public void fromShareJoinGroup(String groupID) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            db.collection("groups").document(groupID)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentReference docRef2 = db.collection("users")
                                    .document(Objects.requireNonNull(user))
                                    .collection("groups")
                                    .document(groupID);
                            docRef2.get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("id", groupID);
                                    docRef2.set(data);
                                    data.clear();
                                    data.put("id", user);
                                    data.put("priority", 2);
                                    db.collection("groups")
                                            .document(groupID)
                                            .collection("members")
                                            .document(user)
                                            .set(data).addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {
                                                    listener.setFromShareJoinedGroup();
                                                }
                                    });
                                }
                            });
                        }
            });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}