package com.example.piston.main.profile;

import com.example.piston.authentication.register.RegisterResult;
import com.example.piston.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        void setBirthDateStatus(RegisterResult.BirthDateError birthDateError);
        void setLoadingFinished();
    }

    public ProfileRepository(IProfile listener) {
        this.listener = listener;
    }


    public void loadProfile(){
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
            listener.setBirthDateStatus(RegisterResult.BirthDateError.NONE);
        else
            listener.setBirthDateStatus(RegisterResult.BirthDateError.INVALID);
    }

    public void editName(String text) {
        editField("name", text);
    }

    public void editPhone(String text) {
        editField("phoneNumber", text);
    }

    public void editBirth(String text) {
        try {
            editField("birthDate", new SimpleDateFormat("dd/MM/yyyy").parse(text));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void editField(String field, Object data) {
        String currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        DocumentReference docRef = db.collection("users")
                .document(Objects.requireNonNull(currentUser));

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            docRef.update(field, data);
            listener.setLoadingFinished();
        });
    }

}
