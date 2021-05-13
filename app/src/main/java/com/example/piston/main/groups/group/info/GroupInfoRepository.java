package com.example.piston.main.groups.group.info;

import android.util.Log;

import com.example.piston.data.Group;
import com.example.piston.data.GroupMember;
import com.example.piston.main.posts.PostRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class GroupInfoRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final GroupInfoRepository.IGroupInfo listener;
    private final DocumentReference docRef;
    private ListenerRegistration listenerRegistration;

    private final String groupID;

    public interface IGroupInfo {
        void setParams(String title, String decription, String imageLink, String groupID);
        void setMembers(ArrayList<GroupMember> members);
    }

    public GroupInfoRepository(GroupInfoRepository.IGroupInfo listener, String groupID) {
        this.listener = listener;
        this.groupID = groupID;

        docRef = db.collection("groups").document(groupID);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setParams((String) Objects.requireNonNull(task.getResult()).get("title"),
                        (String) task.getResult().get("description"),
                        (String) task.getResult().get("imageLink"), groupID);
            }
        });
        listenChanges();
    }

    public void loadMembers() {
        docRef.collection("members").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<GroupMember> members = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    db.collection("users").document(documentSnapshot.getId())
                            .get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    GroupMember member = task1.getResult().toObject(GroupMember.class);
                                    members.add(member);
                                    listener.setMembers(members);
                                }
                    });
                }
            }
        });
    }

    private void listenChanges() {
        listenerRegistration = docRef.collection("members")
                .addSnapshotListener((snapshots, e) -> GroupInfoRepository.this.loadMembers());
    }

    public void deleteGroup() {
        docRef.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Delete post inside group
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    String id = documentSnapshot.getId();
                    deleteLiked(id);
                    DocumentReference docRef1 = docRef.collection("posts").document(id);

                    // Delete replies inside post
                    docRef1.collection("replies")
                            .get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot1 : Objects.requireNonNull(
                                    task1.getResult())) {
                                docRef1.collection("replies")
                                        .document(snapshot1.getId())
                                        .delete();
                            }
                        }
                    });
                    docRef1.delete();
                }
            }
        });

        deleteMembers("owner");
        deleteMembers("mods");
        deleteMembers("members");

        docRef.delete();
    }

    private void deleteLiked(String postID) {
        docRef.collection("members").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Remove liked post from User's collection
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    DocumentReference docRef1 = db.collection("users")
                            .document(documentSnapshot.getId())
                            .collection("liked")
                            .document(postID);

                    docRef1.get().addOnCompleteListener(task1 -> {
                                if (task1.getResult().exists())
                                    docRef1.delete();
                    });

                }
            }
        });
    }

    private void deleteMembers(String collection) {
        docRef.collection(collection).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    String id = documentSnapshot.getId();
                    if (collection.equals("members")) {
                        // Delete groupID from group's members
                        db.collection("users")
                                .document(id)
                                .collection("groups")
                                .document(groupID)
                                .delete();
                    }
                    // Delete owner/member/mod from group
                    docRef.collection(collection)
                            .document(id)
                            .delete();
                }
            }
        });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
