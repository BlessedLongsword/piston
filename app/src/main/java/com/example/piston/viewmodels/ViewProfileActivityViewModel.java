package com.example.piston.viewmodels;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.R;
import com.example.piston.data.ProfileResult;
import com.example.piston.data.repositories.ProfileRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewProfileActivityViewModel extends ViewModel implements ProfileRepository.IProfile{

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> fullName = new MutableLiveData<>("");
    private final MutableLiveData<String> phone = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> birthDate = new MutableLiveData<>("");

    private final MutableLiveData<ProfileResult.BirthDateError> birthDateError = new MutableLiveData<>(ProfileResult.BirthDateError.NONE);

    private final ProfileRepository profileRepository = new ProfileRepository(this);

    public void viewProfile() {
        profileRepository.viewProfile();
    }

    public void birthdayUpdate() {
        profileRepository.checkBirthDate(birthDate.getValue());
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getFullName() {
        return fullName;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getBirthDate() {
        return birthDate;
    }

    public MutableLiveData<ProfileResult.BirthDateError> getBirthDateError() {
        return birthDateError;
    }

    @Override
    public void setUserNameField(String username) {
        this.username.setValue(username);
    }

    @Override
    public void setEmailField(String email) {
        this.email.setValue(email);
    }

    @Override
    public void setFullNameField(String fullName) {
        this.fullName.setValue(fullName);
    }

    @Override
    public void setBirthDateField(Date birthDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.birthDate.setValue(formatter.format(birthDate));
    }

    @Override
    public void setPhoneNumberField(String phoneNumber) {
        this.phone.setValue(phoneNumber);
    }

    @Override
    public void setBirthDateStatus(ProfileResult.BirthDateError birthDateError) {
        this.birthDateError.setValue(birthDateError);
    }



    /*public MutableLiveData<Boolean> getLoading() {
        return loading;
    }*/

}
