package com.example.piston.authentication.googleRegister;

import com.example.piston.authentication.register.RegisterResult;
import com.example.piston.data.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleRegisterRepository {

    private final IGoogleRegister listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public interface IGoogleRegister {
        void setUsernameErrorStatus(RegisterResult.UsernameError usernameError);
        void setBirthDateStatus(RegisterResult.BirthDateError birthDateError);
        void setLoadingFinished();
        void setRegisterFinished();
    }

    public GoogleRegisterRepository(IGoogleRegister listener) {
        this.listener = listener;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    }

    public void register(String username, String birthDate, String idToken) throws ParseException {
        DocumentReference docRef = db.collection("emails").document(username);
        Date userBirthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthDate);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            String email = task.getResult().getUser().getEmail();
            User user = new User(username, email, null, userBirthDate);

            docRef.get().addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                    DocumentSnapshot ds = task2.getResult();
                    if (ds.exists()) {
                        mAuth.signOut();
                        listener.setUsernameErrorStatus(RegisterResult.UsernameError.EXISTS);
                    }
                    else {
                        db.collection("users").document(email).set(user);
                        Map<String, Object> data = new HashMap<>();
                        data.put("email", email);
                        db.collection("emails").document(username).set(data);
                        listener.setLoadingFinished();
                        listener.setRegisterFinished();
                    }
                }
            });
        });
    }

    public void checkUsername(String username) {
        if (username.trim().equals("")) {
            listener.setUsernameErrorStatus(RegisterResult.UsernameError.EMPTY);
        } else {
            DocumentReference docRef = db.collection("emails").document(username);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds.exists())
                        listener.setUsernameErrorStatus(RegisterResult.UsernameError.EXISTS);
                    else
                        listener.setUsernameErrorStatus(RegisterResult.UsernameError.NONE);
                }
            });
        }
    }

    public void checkBirthDate(String birthDate) {
        Pattern pattern = Pattern.compile("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)");
        Matcher matcher = pattern.matcher(birthDate);
        if (matcher.matches())
            listener.setBirthDateStatus(RegisterResult.BirthDateError.NONE);
        else
            listener.setBirthDateStatus(RegisterResult.BirthDateError.INVALID);
    }

}
