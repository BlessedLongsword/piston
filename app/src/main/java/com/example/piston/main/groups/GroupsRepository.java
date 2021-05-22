package com.example.piston.main.groups;

import com.example.piston.data.sections.Group;
import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GroupsRepository {

    private final IGroup listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistration;
    private Query groupsQuery;
    private final String user;

    private Group[] groups;
    private int counter;
    private int lastRequest = 0;

    public interface IGroup {
        void setGroups(ArrayList<Group> groups);
        void setFilter(String filter);
    }

    public GroupsRepository(IGroup listener) {
        this.listener = listener;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        this.groupsQuery = db.collection("users").document(Objects.requireNonNull(user))
                .collection("groups").orderBy("timestamp", Query.Direction.DESCENDING);

        listenChanges();
    }

    private void loadGroups() {
        groupsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int size = Objects.requireNonNull(task.getResult()).size();
                counter = 0;
                groups = new Group[size];
                final int requestNumber = ++lastRequest;
                int position = 0;
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    final int positionActual = position++;
                    DocumentReference docRef = db.collection("groups")
                            .document(documentSnapshot.getId());
                    docRef.get().addOnCompleteListener(task1 -> {
                        DocumentSnapshot ds = task1.getResult();
                        if (Objects.requireNonNull(ds).exists())
                            addGroup(positionActual, ds.toObject(Group.class), requestNumber);
                    });
                }
            }
        });
    }

    private void addGroup(int position, Group group, int requestNumber) {
        if (requestNumber == lastRequest) {
            groups[position] = group;
            if (++counter == groups.length)
                listener.setGroups(new ArrayList<>(Arrays.asList(groups)));
        }
    }

    public void updateQuery(String field, boolean descending) {
        if (field.equals(Values.FILTER_DEFAULT))
            groupsQuery = db.collection("users").document(Objects.requireNonNull(user))
                    .collection("groups").orderBy("timestamp", Query.Direction.DESCENDING);
        else
            groupsQuery = db.collection("users").document(Objects.requireNonNull(user))
                    .collection("groups").orderBy(field, (descending) ?
                    Query.Direction.DESCENDING : Query.Direction.ASCENDING)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
        loadGroups();
        listener.setFilter(field);
    }

    private void listenChanges() {
        listenerRegistration = groupsQuery.addSnapshotListener((snapshots, e) -> GroupsRepository.this.loadGroups());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
