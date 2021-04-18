package com.example.piston.data.repositories;

import android.util.Log;
import android.util.Patterns;

import com.example.piston.data.RegisterResult;
import com.example.piston.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterRepository {

    private final IRegister listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface IRegister {
        void setUsernameErrorStatus(RegisterResult.UsernameError usernameError);
        void setEmailErrorStatus(RegisterResult.EmailError emailError);
        void setPasswordStatus(RegisterResult.PasswordError passwordError);
        void setConfirmPasswordStatus(RegisterResult.ConfirmPasswordError confirmPasswordError);
        void setBirthDateStatus(RegisterResult.BirthdayError birthdayError);
    }

    public RegisterRepository(IRegister listener) {
        this.listener = listener;
    }

    public void register(String username, String email, String password, String birthDate) throws ParseException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("emails").document(username);
        Date userBirthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthDate);
        User user = new User(username, email, password, userBirthDate);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setUsernameErrorStatus(RegisterResult.UsernameError.EXISTS);
            } else {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(regTask -> {
                            if (regTask.isSuccessful()) {
                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(documentReference -> Log.d("DBWriteTAG", "DocumentSnapshot " +
                                                "written with ID: " + documentReference.getId()))
                                        .addOnFailureListener(e -> Log.w("DBWriteTAG", "Error adding document", e));
                            } else {
                                listener.setEmailErrorStatus(RegisterResult.EmailError.EXISTS);
                            }
                        });
            }
        });
    }

    public void checkUsername(String username) {
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

    public void checkEmail(String email) {
        if (email.trim().equals(""))
            listener.setEmailErrorStatus(RegisterResult.EmailError.EMPTY);
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            listener.setEmailErrorStatus(RegisterResult.EmailError.INVALID);
        else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().getSignInMethods().isEmpty())
                        listener.setEmailErrorStatus(RegisterResult.EmailError.EXISTS);
                    else
                        listener.setEmailErrorStatus(RegisterResult.EmailError.NONE);
                }
            });
        }
    }

    public void checkPassword(String password) {
        if (password.trim().equals(""))
            listener.setPasswordStatus(RegisterResult.PasswordError.EMPTY);
        else if (password.length() < 6)
            listener.setPasswordStatus(RegisterResult.PasswordError.INVALID);
        else
            listener.setPasswordStatus(RegisterResult.PasswordError.NONE);
    }

    public void checkConfirmPassword(String password, String confirmPassword) {
        if (!confirmPassword.equals(password))
            listener.setConfirmPasswordStatus(RegisterResult.ConfirmPasswordError.INVALID);
        else
            listener.setConfirmPasswordStatus(RegisterResult.ConfirmPasswordError.NONE);
    }

    public void checkBirthDate(String birthDate) {
        Pattern pattern = Pattern.compile("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)");
        Matcher matcher = pattern.matcher(birthDate);
        if (matcher.matches())
            listener.setBirthDateStatus(RegisterResult.BirthdayError.NONE);
        else
            listener.setBirthDateStatus(RegisterResult.BirthdayError.INVALID);
    }

}
