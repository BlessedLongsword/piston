package com.example.piston.authentication.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.authentication.googleRegister.GoogleRegisterActivity;
import com.example.piston.databinding.ActivityLoginBinding;
import com.example.piston.authentication.register.RegisterActivity;
import com.example.piston.main.MainActivity;

import com.example.piston.utilities.Values;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.signInButtonGoogle.setOnClickListener(view -> signIn());

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult.isSignedIn())
                goToMainActivity();
            if (loginResult.isNewUser())
                registerGoogleUser();
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private static final int RC_REGISTER = 4097;

    private void registerGoogleUser() {
        Intent intent = new Intent(this, GoogleRegisterActivity.class);
        intent.putExtra(Values.ID_TOKEN, idToken);
        startActivityForResult(intent, RC_REGISTER);
    }

    private static final int RC_SIGN_IN = 9001;

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            idToken = Objects.requireNonNull(GoogleSignIn.getSignedInAccountFromIntent(data).getResult()).getIdToken();
            loginViewModel.signInWithToken(idToken);
        }
        else if (requestCode == RC_REGISTER && resultCode==Activity.RESULT_OK)
            goToMainActivity();
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
