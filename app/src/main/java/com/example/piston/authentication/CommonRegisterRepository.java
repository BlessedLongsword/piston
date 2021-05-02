package com.example.piston.authentication;

import com.example.piston.authentication.register.RegisterResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonRegisterRepository {

    protected final ICommonRegister listener;
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface ICommonRegister {
        void setUsernameErrorStatus(RegisterResult.UsernameError usernameError);
        void setBirthDateStatus(RegisterResult.BirthDateError birthDateError);
        void setLoadingFinished();
        void setRegisterFinished();
    }

    public CommonRegisterRepository(ICommonRegister listener) {
        this.listener = listener;
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
