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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.model.User;
import com.example.piston.view.MainActivity;
import com.example.piston.viewmodel.LoginActivityViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel loginActivityViewModel;

    private TextInputLayout username, pwd;
    private final String userCachePath = "logged_user";
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void login(View view) {
        loginActivityViewModel.login(username.getEditText().getText().toString(), pwd.getEditText().getText().toString());
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    //testing pop up window reply post method -> not fully working -> EDIT: it kinda workds!?
    public void test(View anchorView) {

        View popupView = getLayoutInflater().inflate(R.layout.reply_post_youtube, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);
        // If you need the PopupWindow to dismiss when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());
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

        // close keyboard when popup window is closed
        popupWindow.setOnDismissListener(() -> imm.hideSoftInputFromWindow(anchorView.getWindowToken(), 0));
    }

}
