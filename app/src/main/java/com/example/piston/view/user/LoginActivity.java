package com.example.piston.view.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.model.User;
import com.example.piston.view.MainActivity;
import com.example.piston.viewmodel.LoginActivityViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginActivityViewModel loginActivityViewModel;

    private TextInputLayout username, pwd;
    private final String userCachePath = "logged_user";
    private Button signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton2 = findViewById(R.id.sign_in_button_google);
        signInButton2.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button_google).setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        this.username = findViewById(R.id.user_textField);
        this.pwd = findViewById(R.id.pass_textField);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setEnabled(false);

        loginActivityViewModel.getLoginResult().observe(this, loginResult -> {
            username.setError(null);
            pwd.setError(null);
            if (loginResult == null) {
                return;
            }
            if (loginResult.getError() != null) {
                if (loginResult.getError() == R.string.wrong_user) {
                    username.setError(getString(R.string.wrong_user));
                }
                else if (loginResult.getError()== R.string.wrong_password)
                    pwd.setError(getString(R.string.wrong_password));
            }
            if (loginResult.getSuccess() != null) {
                setResult(Activity.RESULT_OK);
                startActivity(new Intent(this, MainActivity.class));
                try {
                    File cacheFile = new File(getApplicationContext().getCacheDir(), userCachePath);
                    String userFile = "userFile";
                    FileOutputStream fOut = openFileOutput(userFile, Context.MODE_PRIVATE);
                    User user = new User("admin", "admin@gmail.com",
                            new Date(System.currentTimeMillis()), "admin");
                    ObjectOutputStream oos = new ObjectOutputStream(fOut);
                    oos.writeObject(user);
                    oos.close();
                    fOut.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean enabled = username.getEditText().length()>0 && pwd.getEditText().length()>0;
                signInButton.setEnabled(enabled);
            }
        };
        username.getEditText().addTextChangedListener(afterTextChangedListener);
        pwd.getEditText().addTextChangedListener(afterTextChangedListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button_google:
                Log.d("nowaybro", "Es crida!!");
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
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
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("nowaybro", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("nowaybro", user.getEmail());
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("nowaybro", "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
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

    private void updateUI(FirebaseUser account) {
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
                });
        Log.d("nowaybro", "Sikeee");
    }


    public void login(View view) {
        startSignIn();
        //loginActivityViewModel.login(username.getEditText().getText().toString(), pwd.getEditText().getText().toString());
    }

    private static final int RC_SIGN_IN = 9001;

    private void startSignIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Sign in with FirebaseUI
        /*Intent intent = FirebaseUtil.getAuthUI()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);*/
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    //testing pop up window reply post method -> not fully working -> EDIT: it kinda workds!?
    public void test(View anchorView) {

        View popupView = getLayoutInflater().inflate(R.layout.reply_post_youtube, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // If you need the PopupWindow to dismiss when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popup_window_animation_slide);
        // Adjust popup window location when keyboard pops up :)))
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);

        // Initialize objects from layout
        TextInputLayout ed = popupView.findViewById(R.id.popup);
        ed.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(ed.getEditText().getText().toString().length() > 0) {
                    ed.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ed.setEndIconDrawable(R.drawable.outline_send_black_36);
                }
                else {
                    ed.setEndIconMode(TextInputLayout.END_ICON_NONE);
                }
            }
        });

        ed.getEditText().requestFocus();
        popupWindow.setOutsideTouchable(false);

        // force show keyboar once pop up window is open
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
    }

}
