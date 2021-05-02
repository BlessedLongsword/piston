package com.example.piston.authentication.googleRegister;

import com.example.piston.authentication.CommonRegisterRepository;
import com.example.piston.authentication.register.RegisterResult;
import com.example.piston.data.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GoogleRegisterRepository extends CommonRegisterRepository {

    private final IGoogleRegister listener;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public interface IGoogleRegister extends ICommonRegister {
    }

    public GoogleRegisterRepository(IGoogleRegister listener) {
        super(listener);
        this.listener = listener;
    }

    public void register(String username, String birthDate, String idToken) throws ParseException {
        DocumentReference docRef = db.collection("emails").document(username);
        Date userBirthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthDate);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            String email = task.getResult().getUser().getEmail();
            User user = new User(username, email, userBirthDate);

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
}
