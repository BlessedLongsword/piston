package com.example.piston.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AuthRepository {

    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> signedIn;

    public AuthRepository(MutableLiveData<Boolean> signedIn) {
        mAuth = FirebaseAuth.getInstance();
        this.signedIn = signedIn;
    }

    public void checkIfUserIsAuthenticatedInFirebase() {
        signedIn.setValue(mAuth.getCurrentUser()!=null);
    }

    public void signInWithGoogle(Task<GoogleSignInAccount> task) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d("nowaybro", "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w("nowaybro", "Google sign in failed", e);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("nowaybro", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("nowaybro", user.getEmail());
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("nowaybro", "signInWithCredential:failure", task.getException());
                        //updateUI(null);
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("nowaybro", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    MutableLiveData<FirebaseUser> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {
        MutableLiveData<FirebaseUser> authenticatedUserMutableLiveData = new MutableLiveData<>();
        mAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    //User user = new User(uid, name, email);
                    //user.isNew = isNewUser;
                    //authenticatedUserMutableLiveData.setValue(user);
                }
            } else {
                Log.d("nowaybro", authTask.getException().getMessage());
            }
        });
        return authenticatedUserMutableLiveData;
    }

    public void login(String username, String password) {
        if (!username.contains("@"))
            getEmail(username, password);
        else
            loginEmail(username, password);
    }

    private void loginEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("nowaybro", "signInWithEmailAndPassword:success");
                        signedIn.setValue(true);
                    } else {
                        Log.w("nowaybro", "signInWithEmailAndPassword:failure",
                                task.getException());
                    }
                });
    }

    private String getEmail(String username, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("emails").document(username);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("nowaybro", "DocumentSnapshot data: " + document.getData());
                    String email = document.get("email").toString();
                    loginEmail(email, password);
                    Log.d("nowaybro", "yay: " + email);
                    //return email;
                } else {
                    Log.d("nowaybro", "No such document");
                }
            } else {
                Log.d("nowaybro", "get failed with ", task.getException());
            }
        });
        return null;

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
    }

    public void logout() {
        mAuth.signOut();
        signedIn.setValue(false);
    }



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