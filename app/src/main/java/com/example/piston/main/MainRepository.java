package com.example.piston.main;

import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

public class MainRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final IMain listener;
    private final CollectionReference userGroupsCollection;
    private ListenerRegistration listenerRegistration;

    public interface IMain {
        void setSignedIn(boolean signedIn);
        void setFromShareBelongsToGroup(boolean belongsToGroup);
    }

    public MainRepository(IMain listener) {
        this.listener = listener;
        userGroupsCollection = db.collection("users").document(Objects.requireNonNull
                (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection(Values.GROUPS);
        listenChanges();
    }

    public void checkFromShareBelongsToGroup(String groupID) {
        userGroupsCollection.document(groupID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        listener.setFromShareBelongsToGroup(task.getResult().exists());
        });
    }

    public void updateGroupParamsForCurrentUser() {
        userGroupsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot ds : task.getResult()) {
                    db.collection(Values.GROUPS).document(ds.getId()).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentReference dr = ds.getReference();
                            dr.get().addOnCompleteListener(task2 -> {
                               if (task2.isSuccessful()) {
                                   dr.update("title", task1.getResult().get("title"));
                                   dr.update("numMembers", task1.getResult().get("numMembers"));
                               }
                            });
                        }
                    });
                }
            }
        });
    }

    private void listenChanges() {
        listenerRegistration = db.collection(Values.GROUPS).addSnapshotListener((snapshots, e) ->
                MainRepository.this.updateGroupParamsForCurrentUser());
    }

    private void removeListener() {
        listenerRegistration.remove();
    }

    public void logout() {
        removeListener();
        mAuth.signOut();
        listener.setSignedIn(false);
    }

}
