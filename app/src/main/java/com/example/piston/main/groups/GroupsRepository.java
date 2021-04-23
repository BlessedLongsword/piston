package com.example.piston.main.groups;

import android.util.Log;

import com.example.piston.data.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class GroupsRepository {

    private final IGroup listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
                        ArrayList<Group> groups = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            DocumentReference docRef = db.collection("groups")
                                    .document(documentSnapshot.getId());
                            docRef.get().addOnCompleteListener(task1 -> {
                                DocumentSnapshot ds = task1.getResult();
                                if (Objects.requireNonNull(ds).exists()) {
                                    groups.add(ds.toObject(Group.class));
                                } else {
                                    Log.w("DBReadTAG", "Error getting data: ", task1.getException());
                                }
                            });
                        }
                        listener.setGroups(groups);
                    } else {
                        Log.w("DBReadTAG", "Error getting data: ", task.getException());
                    }
                });
    }

    private void listenChanges() {
        db.collection("groups")
                .addSnapshotListener((snapshots, e) -> GroupsRepository.this.loadGroups());
    }

}
