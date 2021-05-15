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
        void setPriority(Integer priority);
        void setFinished(boolean finished);
    }

    public GroupInfoRepository(GroupInfoRepository.IGroupInfo listener, String groupID) {
        this.listener = listener;
        this.groupID = groupID;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        docRef = db.collection("groups").document(groupID);

        updateParams();
        isOwner();
        listenChanges();
    }

    public void updateParams() {
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setParams((String) Objects.requireNonNull(task.getResult()).get("title"),
                        (String) task.getResult().get("description"),
                        (String) task.getResult().get("imageLink"), groupID);
            }
        });
    }

    public void editDescription(String text) {
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            docRef.update("description", text);
            listener.setFinished(true);
        });
    }

    public void editTitle(String text) {
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            docRef.update("title", text);
            listener.setFinished(true);
        });
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

    public void removeMember(String memberEmail) {
        docRef.collection("members").document(memberEmail).delete();
        db.collection("users").document(memberEmail)
                .collection("groups").document(groupID).delete();
    }

    public void updateMemberPriority(String memberEmail, int priority) {
        DocumentReference memberDocRef = docRef.collection("members").document(memberEmail);
        memberDocRef.get().addOnSuccessListener(documentSnapshot -> memberDocRef.update("priority", priority));
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

                    docRef1.collection("userLikes")
                            .get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot1 : Objects.requireNonNull(
                                    task2.getResult())) {
                                db.collection("users").document(snapshot1.getId())
                                        .collection("liked")
                                        .document(id)
                                        .delete();

                                docRef1.collection("userLikes")
                                        .document(snapshot1.getId())
                                        .delete();
                            }
                        }
                    });
                    
                    docRef1.delete();
                }
            }
        });

        deleteMembers();

        docRef.delete(); // Delete group
    }

    private void deleteMembers() {
        docRef.collection("members").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    String id = documentSnapshot.getId();

                    // Delete groupID from group's members
                    db.collection("users")
                            .document(id)
                            .collection("groups")
                            .document(groupID)
                            .delete();

                    // Delete members from group
                    docRef.collection("members")
                            .document(id)
                            .delete();
                }
            }
        });
    }

    public void isOwner() {
        docRef.collection("members")
                .document(user)
                .get().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        long priority = (long) Objects.requireNonNull(task.getResult().get("priority"));
                        listener.setPriority((int) priority);
                    }
        });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
