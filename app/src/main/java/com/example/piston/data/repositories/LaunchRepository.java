package com.example.piston.data.repositories;

import com.google.firebase.auth.FirebaseAuth;

public class LaunchRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final LaunchRepository.ILaunch listener;

    public interface ILaunch {
        void setIsSignedId(boolean isSignedIn);
    }

    public LaunchRepository(ILaunch listener) {
        this.listener = listener;
    }

    public void checkIfUserIsAuthenticated() {
        listener.setIsSignedId(mAuth.getCurrentUser()!=null);
    }

}
