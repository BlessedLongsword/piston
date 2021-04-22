package com.example.piston.main;

import com.google.firebase.auth.FirebaseAuth;

public class MainRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final IMain listener;

    public interface IMain {
        void setSignedIn(boolean signedIn);
    }

    public MainRepository(IMain listener) {
        this.listener = listener;
    }

    public void logout() {
        mAuth.signOut();
        listener.setSignedIn(false);
    }

}
