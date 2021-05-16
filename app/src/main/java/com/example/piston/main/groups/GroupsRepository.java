package com.example.piston.main.groups;

import com.example.piston.data.sections.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GroupsRepository {

    private final IGroup listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistration;
    private final CollectionReference groupsColRef;

    private Group[] groups;
    private int counter;

    public interface IGroup {
        void setGroups(ArrayList<Group> groups);
    }

    public GroupsRepository(IGroup listener) {
        this.listener = listener;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        this.groupsColRef = db.collection("users").document(Objects.requireNonNull(user)).collection("groups");

        listenChanges();
    }

    private void loadGroups() {
        groupsColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int size = Objects.requireNonNull(task.getResult()).size();
                counter = 0;
                groups = new Group[size];
                int position = 0;
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    final int positionActual = position++;
                    DocumentReference docRef = db.collection("groups")
                            .document(documentSnapshot.getId());
                    docRef.get().addOnCompleteListener(task1 -> {
                        DocumentSnapshot ds = task1.getResult();
                        if (Objects.requireNonNull(ds).exists())
                            addGroup(positionActual, ds.toObject(Group.class));
                    });
                }
            }
        });
    }

    private void addGroup(int position, Group group) {
        groups[position] = group;
        if (++counter == groups.length)
            listener.setGroups(new ArrayList<>(Arrays.asList(groups)));
    }

    private void listenChanges() {
        listenerRegistration = groupsColRef.addSnapshotListener((snapshots, e) -> GroupsRepository.this.loadGroups());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
