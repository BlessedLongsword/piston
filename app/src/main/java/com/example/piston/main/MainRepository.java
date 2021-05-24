package com.example.piston.main;

import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final IMain listener;
    private final CollectionReference userGroupsCollection;

    public interface IMain {
        void setSignedIn(boolean signedIn);
        void setFromShareBelongsToGroup(boolean belongsToGroup);
    }

    public MainRepository(IMain listener) {
        this.listener = listener;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userGroupsCollection = db.collection("users").document(Objects.requireNonNull
                (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection(Values.GROUPS);
    }

    public void checkFromShareBelongsToGroup(String groupID) {
        userGroupsCollection.document(groupID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        listener.setFromShareBelongsToGroup(task.getResult().exists());
        });
    }

    public void logout() {
        mAuth.signOut();
        listener.setSignedIn(false);
    }

}
