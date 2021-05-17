package com.example.piston.authentication.register;

import android.annotation.SuppressLint;
import android.util.Patterns;

import com.example.piston.authentication.CommonRegisterRepository;
import com.example.piston.data.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterRepository extends CommonRegisterRepository {

    private final IRegister listener;

    public interface IRegister extends ICommonRegister {
        void setEmailErrorStatus(RegisterResult.EmailError emailError);
        void setPasswordStatus(RegisterResult.PasswordError passwordError);
        void setConfirmPasswordStatus(RegisterResult.ConfirmPasswordError confirmPasswordError);
    }

    public RegisterRepository(IRegister listener) {
        super(listener);
        this.listener = listener;
    }

    public void register(String username, String email, String password, String birthDate) throws ParseException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("emails").document(username);
        @SuppressLint("SimpleDateFormat") Date userBirthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthDate);
        User user = new User(username, email, userBirthDate);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot ds = task.getResult();
                if (Objects.requireNonNull(ds).exists())
                    listener.setUsernameErrorStatus(RegisterResult.UsernameError.EXISTS);
                else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(regTask -> {
                                if (regTask.isSuccessful()) {
                                    db.collection("users").document(email).set(user);
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("email", email);
                                    db.collection("emails").document(username).set(data);
                                    listener.setLoadingFinished();
                                    listener.setRegisterFinished();
                                } else {
                                    try {
                                        throw Objects.requireNonNull(regTask.getException());
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        listener.setEmailErrorStatus(RegisterResult.EmailError.INVALID);
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        listener.setEmailErrorStatus(RegisterResult.EmailError.EXISTS);
                                    } catch (Exception e) {
                                        listener.setEmailErrorStatus(RegisterResult.EmailError.UNEXPECTED);
                                    } finally {
                                        listener.setLoadingFinished();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void checkEmail(String email) {
        if (email.trim().equals(""))
            listener.setEmailErrorStatus(RegisterResult.EmailError.EMPTY);
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            listener.setEmailErrorStatus(RegisterResult.EmailError.INVALID);
        }
        else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty())
                        listener.setEmailErrorStatus(RegisterResult.EmailError.NONE);
                    else {
                        listener.setEmailErrorStatus(RegisterResult.EmailError.EXISTS);
                    }
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

}
