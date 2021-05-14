package com.example.piston.main.groups.group.info;

import com.example.piston.data.GroupMember;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GroupInfoRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final GroupInfoRepository.IGroupInfo listener;
    private final DocumentReference docRef;
    private ListenerRegistration listenerRegistration;

    private final String groupID, user;
    private GroupMember[] members;
    private int counter;

    public interface IGroupInfo {
        void setParams(String title, String description, String imageLink, String groupID);
        void setMembers(ArrayList<GroupMember> members);
        void setIsOwner(boolean priority);
    }

    public GroupInfoRepository(GroupInfoRepository.IGroupInfo listener, String groupID) {
        this.listener = listener;
        this.groupID = groupID;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        docRef = db.collection("groups").document(groupID);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setParams((String) Objects.requireNonNull(task.getResult()).get("title"),
                        (String) task.getResult().get("description"),
                        (String) task.getResult().get("imageLink"), groupID);
            }
        });
        isOwner();
        listenChanges();
    }

    public void loadMembers() {
        docRef.collection("members").orderBy("priority").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int size = Objects.requireNonNull(task.getResult()).size();
                counter = 0;
                members = new GroupMember[size];
                int position = 0;
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    final int currentPosition = position;
                    db.collection("users").document(documentSnapshot.getId())
                            .get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    GroupMember member = task1.getResult().toObject(GroupMember.class);
                                    long priority = (long) Objects.requireNonNull(documentSnapshot.get("priority"));
                                    Objects.requireNonNull(member).setPriority((int) priority);
                                    addMember(currentPosition, member);
                                }
                    });
                    position++;
                }
            }
        });
    }

    private void addMember(int position, GroupMember member) {
        members[position] = member;
        if (++counter == members.length)
            listener.setMembers(new ArrayList<>(Arrays.asList(members)));
    }

    private void listenChanges() {
        listenerRegistration = docRef.collection("members")
                .addSnapshotListener((snapshots, e) -> GroupInfoRepository.this.loadMembers());
    }

    public void deleteGroup() {
        docRef.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Delete posts inside group
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

        docRef.delete(); // Delete group
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

    public void isOwner() {

        docRef.collection("members")
                .document(user).get().addOnCompleteListener(task -> {
                                    if (task.isComplete()) {
                                        long priority = (long) Objects.requireNonNull(task.getResult().get("priority"));
                                        listener.setIsOwner((int) priority == 0);
                                    }
        });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
