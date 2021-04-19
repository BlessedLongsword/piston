package com.example.piston.data.repositories;

import com.example.piston.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class ProfileRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final IProfile listener;

    public interface IProfile {
        void setSignedIn(boolean signedIn);
        void setUserNameField(String username);
        void setEmailField(String email);
        void setFullNameField(String fullName);
        void setBirthDateField(Date birthDate);
        void setPhoneNumberField(String phoneNumber);
    }

    public ProfileRepository(IProfile listener) {
        this.listener = listener;
    }


    public void viewProfile(){
        String currentUser = mAuth.getCurrentUser().getEmail();
        db.collection("users").document(currentUser).get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            listener.setUserNameField(user.getUsername());
            listener.setEmailField(user.getEmail());
            listener.setFullNameField(user.getFullName());
            listener.setBirthDateField(user.getBirthDate());
            listener.setPhoneNumberField(user.getPhoneNumber());
        });
    }



}
