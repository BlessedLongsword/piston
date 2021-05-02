package com.example.piston.authentication.googleRegister;

import android.util.Log;

import com.example.piston.authentication.CommonRegisterViewModel;
import com.example.piston.authentication.register.RegisterResult;

import java.util.Objects;

public class GoogleRegisterViewModel extends CommonRegisterViewModel implements GoogleRegisterRepository.IGoogleRegister {

    private final GoogleRegisterRepository registerRepository = new GoogleRegisterRepository(this);
    private final String idToken;

    public GoogleRegisterViewModel(String idToken) {
        this.idToken = idToken;
    }

    public void register() {
        try {
            registerRepository.register(username.getValue(), birthDate.getValue(), idToken);
            loading.setValue(true);
        } catch (Exception e) {
            Log.w("DBWriteTAG", "Something went wrong went converting Date", e);
        }
    }

    public void usernameUpdate() {
        onFieldChanged();
        registerRepository.checkUsername(Objects.requireNonNull(username.getValue()));
    }
    public void birthdayUpdate() {
        onFieldChanged();
        registerRepository.checkBirthDate(birthDate.getValue());
    }

    public void onFieldChanged() {
        registerEnabled.setValue((Objects.requireNonNull(getUsername().getValue().trim()).length() > 0) &&
                (getBirthDate().getValue().trim().length() > 0) &&
                (getCheckEnabled().getValue()) &&
                (getUsernameError().getValue() == RegisterResult.UsernameError.NONE) &&
                (getBirthDateError().getValue() == RegisterResult.BirthDateError.NONE));
    }

}
