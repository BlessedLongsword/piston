package com.example.piston.main.profile;

import com.example.piston.data.User;
import com.example.piston.main.posts.createPost.CreatePostResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final IProfile listener;

    public interface IProfile {
        void setUserNameField(String username);
        void setEmailField(String email);
        void setFullNameField(String fullName);
        void setBirthDateField(Date birthDate);
        void setPhoneNumberField(String phoneNumber);
        void setBirthDateStatus(ProfileResult.BirthDateError birthDateError);
        void setLoadingFinished();
    }

    public ProfileRepository(IProfile listener) {
        this.listener = listener;
    }


    public void viewProfile(){
        String currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection("users")
                .document(Objects.requireNonNull(currentUser))
                .get().addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    listener.setUserNameField(Objects.requireNonNull(user).getUsername());
                    listener.setEmailField(user.getEmail());
                    listener.setFullNameField(user.getName());
                    listener.setBirthDateField(user.getBirthDate());
                    listener.setPhoneNumberField(user.getPhoneNumber());
        });
    }

    public void checkBirthDate(String birthDate) {
        Pattern pattern = Pattern.compile("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)");
        Matcher matcher = pattern.matcher(birthDate);
        if (matcher.matches())
            listener.setBirthDateStatus(ProfileResult.BirthDateError.NONE);
        else
            listener.setBirthDateStatus(ProfileResult.BirthDateError.INVALID);
    }

    public void editField(String field, String data) {
        String currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        DocumentReference docRef = db.collection("users")
                .document(currentUser);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            docRef.update(field, data);
            viewProfile();
            listener.setLoadingFinished();
        });
    }

}
