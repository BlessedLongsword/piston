package com.example.piston.authentication.login;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final ILogin listener;

    public interface ILogin {
        void setLoginResult(LoginResult loginResult);
    }

    public LoginRepository(ILogin listener) {
        this.listener = listener;
    }

    public void login(String username, String password) {
        if (!username.contains("@"))
            loginUsername(username, password);
        else
            loginEmail(username, password);
    }

    private void loginUsername(String username, String password) {
        DocumentReference docRef = db.collection("emails").document(username);
        docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists()) {
                        String email = Objects.requireNonNull(document.get("email")).toString();
                        loginEmail(email, password);
                    }
                    else {
                        LoginResult loginResult = new LoginResult();
                        loginResult.setUsernameError(LoginResult.UsernameError.INVALID);
                        listener.setLoginResult(loginResult);
                    }
                } else {
                    Log.d("nowaybro", "get failed with ", task.getException());
                }
        });
    }

    private void loginEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        LoginResult loginResult = new LoginResult();
                        loginResult.setSignedIn(true);
                        listener.setLoginResult(loginResult);
                    } else {
                        LoginResult loginResult = new LoginResult();
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthInvalidUserException exception) {
                            loginResult.setUsernameError(LoginResult.UsernameError.INVALID);
                        } catch (FirebaseAuthInvalidCredentialsException exception) {
                        loginResult.setPasswordError(LoginResult.PasswordError.INCORRECT);
                        } catch (Exception exception) {
                            Log.d("nowaybro", "Unexpected exception: " + exception.getMessage());
                        }
                        listener.setLoginResult(loginResult);
                    }
                });
    }

    public void signInWithGoogle(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null)
                firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Log.w("nowaybro", "Google sign in failed", e);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        LoginResult loginResult = new LoginResult();
                        boolean isNewUser = Objects.requireNonNull(Objects.requireNonNull(
                                task.getResult()).getAdditionalUserInfo()).isNewUser();
                        if (isNewUser)
                            loginResult.setNewUser(true);
                        else
                            loginResult.setSignedIn(true);
                        listener.setLoginResult(loginResult);
                    } else {
                        Log.w("nowaybro", "signInWithCredential:failure", task.getException());
                    }
                });
    }

        /*db.collection("emails")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("nowaybro", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w("nowaybro", "Error getting documents.", task.getException());
                }
            });*/

    /*Acces firestore collection users
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("users")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("nowaybro", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w("nowaybro", "Error getting documents.", task.getException());
                }
            });*/

    /*Email verification
    final FirebaseUser user = mAuth.getCurrentUser();
    user.sendEmailVerification()
            .addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    // Re-enable button

                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this,
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("nowaybro", "sendEmailVerification", task.getException());
                        Toast.makeText(MainActivity.this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
}