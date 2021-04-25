package com.example.piston.main.groups;

import android.util.Log;

import com.example.piston.data.Group;
import com.google.firebase.auth.FirebaseAuth;
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
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ListenerRegistration listenerRegistration;

    private Group[] groups;
    private int counter;

    public interface IGroup {
        void setGroups(ArrayList<Group> groups);
    }

    public GroupsRepository(IGroup listener) {
        this.listener = listener;
        listenChanges();
    }

    public void loadGroups() {
        db.collection("users")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("groups")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int size = task.getResult().size();
                        groups = new Group[size];
                        int position = 0;
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            final int positionActual = position++;
                            DocumentReference docRef = db.collection("groups")
                                    .document(documentSnapshot.getId());
                            docRef.get().addOnCompleteListener(task1 -> {
                                DocumentSnapshot ds = task1.getResult();
                                if (Objects.requireNonNull(ds).exists()) {
                                    addGroup(positionActual, ds.toObject(Group.class));
                                } else {
                                    Log.w("DBReadTAG", "Error getting data: ", task1.getException());
                                }
                            });
                        }
                    } else {
                        Log.w("DBReadTAG", "Error getting data: ", task.getException());
                    }
                });
    }

    private void addGroup(int position, Group group) {
        groups[position] = group;
        if (++counter == groups.length)
            listener.setGroups(new ArrayList<>(Arrays.asList(groups)));
    }

    private void listenChanges() {
        listenerRegistration = db.collection("groups")
                .addSnapshotListener((snapshots, e) -> {
                    Log.d("nowaybro", "Group change...");
                    GroupsRepository.this.loadGroups();
                });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
