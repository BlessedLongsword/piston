package com.example.piston.main.groups.group.info;

import android.util.Log;

import com.example.piston.data.Group;
import com.example.piston.data.GroupMember;
import com.example.piston.main.posts.PostRepository;
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

    public interface IGroupInfo {
        void setParams(String title, String decription, String imageLink, String groupID);
        void setMembers(ArrayList<GroupMember> members);
    }

    public GroupInfoRepository(GroupInfoRepository.IGroupInfo listener, String groupID) {
        this.listener = listener;
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
                                    Log.d("DBReadTAG", member.getUsername());
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

    public void removeListener() {
        listenerRegistration.remove();
    }

}
