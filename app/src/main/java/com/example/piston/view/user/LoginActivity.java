package com.example.piston.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityLoginBinding;
import com.example.piston.util.textwatchers.BaseTextWatcher;
import com.example.piston.view.sections.MainActivity;
import com.example.piston.viewmodel.LoginActivityViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel loginActivityViewModel;
    private ActivityLoginBinding binding;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(loginActivityViewModel);
        binding.setActivity(this);
        binding.setLifecycleOwner(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInGoogle = findViewById(R.id.sign_in_button_google);
        signInGoogle.setOnClickListener(this::signIn);

        TextWatcher afterTextChangedListener = new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                binding.getViewModel().onTextChanged();
            }
        };
        binding.userEditText.addTextChangedListener(afterTextChangedListener);
        binding.passEditText.addTextChangedListener(afterTextChangedListener);

        binding.getViewModel().signedIn.observe(this, isSignedIn -> {
            if (isSignedIn)
                goToMainActivity();
        });

    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private static final int RC_SIGN_IN = 9001;

    public void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            loginActivityViewModel.signInWithGoogle(task);
        }
    }


    public void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
