package com.example.piston.model;

import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterRepository {

    private final IRegister listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface IRegister {
        void setUsernameErrorStatus(RegisterResult.UsernameError usernameError);
        void setEmailErrorStatus(RegisterResult.EmailError emailError);
        void setRegisterResult(RegisterResult registerResult);
    }

    public RegisterRepository(IRegister listener) {
        this.listener = listener;
    }

    private void checkUsername(String username) {
        if (username.trim().equals("")) {
            listener.setUsernameErrorStatus(RegisterResult.UsernameError.EMPTY);
        } else {
            DocumentReference docRef = db.collection("emails").document(username);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.setUsernameErrorStatus(RegisterResult.UsernameError.EXISTS);
                } else {
                    listener.setUsernameErrorStatus(RegisterResult.UsernameError.NONE);
                }
            });
        }
    }

    private void checkEmail(String email) {
        //AtomicBoolean exists = new AtomicBoolean(false);
        if (email.trim().equals(""))
            listener.setEmailErrorStatus(RegisterResult.EmailError.EMPTY);
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            listener.setEmailErrorStatus(RegisterResult.EmailError.INVALID);
        else {
            db.collection("emails")
                    .get()
                    .addOnCompleteListener(task -> {
                        boolean exists = false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("email").equals(email)) {
                                    listener.setEmailErrorStatus(RegisterResult.EmailError.EXISTS);
                                    exists = true;
                                }
                            }
                            if (!exists)
                                listener.setEmailErrorStatus(RegisterResult.EmailError.NONE);
                        }
                    });
        }
    }

    private void register(String username, String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db.collection("emails")
                .get()
                .addOnCompleteListener(task -> {
                    boolean usernameExists = false;
                    boolean emailExists = false;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId().equals(username)) {
                                listener.setUsernameErrorStatus(RegisterResult.UsernameError.EXISTS);
                                usernameExists = true;
                            }
                            if (document.getData().get("email").equals(email)) {
                                listener.setEmailErrorStatus(RegisterResult.EmailError.EXISTS);
                                emailExists = true;
                            }
                        }
                        if (emailExists || usernameExists) {
                            if (!usernameExists)
                                listener.setUsernameErrorStatus(RegisterResult.UsernameError.NONE);
                            if (!emailExists)
                                listener.setEmailErrorStatus(RegisterResult.EmailError.NONE);
                        } else
                            firebaseAuth.createUserWithEmailAndPassword(username, password);
                    }
                });
    }

}
