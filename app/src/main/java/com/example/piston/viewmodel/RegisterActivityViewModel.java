package com.example.piston.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.R;
import com.example.piston.model.Result;
import com.example.piston.model.UserList;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivityViewModel extends ViewModel {

    private MutableLiveData<RegisterResult> registerResult;
    private MutableLiveData<RegisterFormState> registerFormState;
    private UserList users;

    public RegisterActivityViewModel() {
        registerResult = new MutableLiveData<>();
        registerFormState = new MutableLiveData<>();
        users = new UserList();
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    public void register(String username, String pwd1, String pwd2, String email, Date birthDate) {
        Result<String> result = users.register(username, pwd1, pwd2, email, birthDate);

        if (result instanceof Result.Success) {
            registerResult.setValue(new RegisterResult(true));
        }
        if(result instanceof Result.Error) {
            Log.d("what"," register" + result.toString());
            if (result.toString().equals("Error[exception=Username required]")) {
                registerResult.setValue(new RegisterResult(R.string.username_req));
            }
            else if (result.toString().equals("Error[exception=Email required]")) {
                registerResult.setValue((new RegisterResult(R.string.email_req)));
            }
            else if (result.toString().equals("Error[exception=Password required]")) {
                registerResult.setValue(new RegisterResult(R.string.pwd_req));
            }
            else if (result.toString().equals("Error[exception=Username already taken]")) {
                registerResult.setValue((new RegisterResult(R.string.invalid_username)));
            }
            else if (result.toString().equals("Error[exception=This email is already taken]")) {
                registerResult.setValue((new RegisterResult(R.string.email_taken)));
            }
            else if (result.toString().equals("Error[exception=Password must be at least 6 characters]")) {
                registerResult.setValue((new RegisterResult(R.string.invalid_password)));
            }
            else if (result.toString().equals("Error[exception=Passwords do not match]")) {
                registerResult.setValue((new RegisterResult(R.string.invalid_password2)));
            }
        }
    }

    public void registerUsernameChanged(String username) {
        if (username == null) {
            return;
        }
        else if (username.trim().length() == 0){
            Log.d("what"," registerUsernameChanged1");
            registerFormState.setValue(new RegisterFormState(null,null,
                    null,null, R.string.username_req,null,
                    null,null));
        }
        else if (users.isValidUsername(username)) {
            Log.d("what"," registerUsernameChanged2");
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username,
                    null,null,null,null,
                    null,null,null));
        }else {
            registerFormState.setValue(new RegisterFormState(null,
                    null,null,null,null,
                    null,null,null));
            Log.d("what"," registerUsernameChanged3");
        }
    }

    public void registerPwd1Changed(String pwd1) {
        if (pwd1 == null) {
            return;
        }
        else if (pwd1.trim().length() == 0) {
            registerFormState.setValue(new RegisterFormState(null,null,
                    null,null,null, R.string.pwd_req,
                    null,null));
        }
        else if (pwd1.length() < 6) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password,
                    null, null,null,null,null,null));
        }
    }

    public void registerPwd2Changed(String pwd1, String pwd2){
        if (pwd2 == null) {
            return;
        }
        else if (!pwd2.equals(pwd1)) {
            registerFormState.setValue(new RegisterFormState(null,null,
                    R.string.invalid_password2,null, null,null,null,null));
        }
    }

    public void registerEmailChanged(String email) {
        if (email == null){
            return;
        }
        else if ( !users.isValidEmail(email)) {
            registerFormState.setValue((new RegisterFormState(null,null,null,
                    null,null,null, R.string.email_req,null)));
        }
        else if (users.emailExists(email)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,
                    R.string.email_taken,null,null,null,null));
        }
    }

    public void registerBdayChanged(String bday) {
        if (bday == null){
            return;
        }
        Pattern pattern = Pattern.compile("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)");
        Matcher matcher = pattern.matcher(bday);
        if (!matcher.matches()) {
            registerFormState.setValue(new RegisterFormState(null,null,null,
                    null,null,null,null,R.string.invalid_date));
        }
    }

}
