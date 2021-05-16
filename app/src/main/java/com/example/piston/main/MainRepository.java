package com.example.piston.main;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final IMain listener;

    public interface IMain {
        void setSignedIn(boolean signedIn);
        void setFromShareBelongsToGroup(boolean belongsToGroup);
    }

    public MainRepository(IMain listener) {
        this.listener = listener;
    }

    public void checkFromShareBelongsToGroup(String groupID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(Objects.requireNonNull
                (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .collection("groups").document(groupID).get()
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
