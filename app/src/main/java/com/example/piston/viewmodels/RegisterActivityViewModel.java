package com.example.piston.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.repositories.RegisterRepository;
import com.example.piston.data.RegisterResult;

import java.util.Objects;

public class RegisterActivityViewModel extends ViewModel implements RegisterRepository.IRegister {

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<String> password2 = new MutableLiveData<>("");
    private final MutableLiveData<String> birthday = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> registerEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> checkEnabled = new MutableLiveData<>(false);

    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>(new RegisterResult());

    private final RegisterRepository registerRepository = new RegisterRepository(this);

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getPassword2() {
        return password2;
    }

    public MutableLiveData<String> getBirthday() {
        return birthday;
    }

    public LiveData<Boolean> getCheckEnabled() {
        return checkEnabled;
    }

    public LiveData<Boolean> getRegisterEnabled() {
        return registerEnabled;
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register() {
        try {
            registerRepository.register(username.getValue(), email.getValue(),
                    password.getValue(), birthday.getValue());
        } catch (Exception e) {
            Log.w("DBWriteTAG", "Something went wrong went converting Date", e);
        }
    }

    public void usernameUpdate() {
        onTextChanged();
    }
    public void passwordUpdate() {
        onTextChanged();
    }
    public void password2Update() {
        onTextChanged();
    }
    public void birthdayUpdate() {
        onTextChanged();
    }
    public void emailUpdate() {
        onTextChanged();
    }

    public void onTextChanged() {
        registerEnabled.setValue(Objects.requireNonNull(getUsername().getValue()).length()>0 &&
                Objects.requireNonNull(getPassword().getValue()).length()>0);
    }

    @Override
    public void setUsernameErrorStatus(RegisterResult.UsernameError usernameError) {
        registerResult.getValue().setUsernameError(usernameError);
        registerResult.notify();
    }

    @Override
    public void setEmailErrorStatus(RegisterResult.EmailError emailError) {
        registerResult.getValue().setEmailError(emailError);
        registerResult.notify();
    }

    @Override
    public void setPasswordStatus(RegisterResult.PasswordError passwordError) {
        registerResult.getValue().setPasswordError(passwordError);
        registerResult.notify();
    }

    @Override
    public void setConfirmPasswordStatus(RegisterResult.ConfirmPasswordError confirmPasswordError) {
        registerResult.getValue().setConfirmPasswordError(confirmPasswordError);
        registerResult.notify();
    }

    @Override
    public void setBirthDateStatus(RegisterResult.BirthdayError birthdayError) {
        registerResult.getValue().setBirthdayError(birthdayError);
        registerResult.notify();
    }



/*

    public void register(String username, String pwd1, String pwd2, String email, Date birthDate) {
        Result<String> result = users.register(username, pwd1, pwd2, email, birthDate);

        if (result instanceof Result.Success) {
            registerResult.setValue(new RegisterResult(true));
        }
        if(result instanceof Result.Error) {
            Log.d("what"," register" + result.toString());
            if (result.toString().equals("UsernameError[exception=Username required]")) {
                registerResult.setValue(new RegisterResult(R.string.username_req));
            }
            else if (result.toString().equals("UsernameError[exception=Email required]")) {
                registerResult.setValue((new RegisterResult(R.string.email_req)));
            }
            else if (result.toString().equals("UsernameError[exception=Password required]")) {
                registerResult.setValue(new RegisterResult(R.string.pwd_req));
            }
            else if (result.toString().equals("UsernameError[exception=Username already taken]")) {
                registerResult.setValue((new RegisterResult(R.string.invalid_username)));
            }
            else if (result.toString().equals("UsernameError[exception=This email is already taken]")) {
                registerResult.setValue((new RegisterResult(R.string.email_taken)));
            }
            else if (result.toString().equals("UsernameError[exception=Password must be at least 6 characters]")) {
                registerResult.setValue((new RegisterResult(R.string.invalid_password)));
            }
            else if (result.toString().equals("UsernameError[exception=Passwords do not match]")) {
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
*/
}
