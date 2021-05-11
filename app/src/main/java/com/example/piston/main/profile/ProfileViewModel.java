package com.example.piston.main.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileViewModel extends ViewModel implements ProfileRepository.IProfile{

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> name = new MutableLiveData<>("");
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> birthDate = new MutableLiveData<>("");

    /*PupUps*/
    private final MutableLiveData<String> editNameText = new MutableLiveData<>("");
    private final MutableLiveData<String> editPhoneNumberText = new MutableLiveData<>("");
    private final MutableLiveData<String> editBirthDateText = new MutableLiveData<>("");
    private final MutableLiveData<ProfileResult.BirthDateError> editBirthDateError = new MutableLiveData<>(ProfileResult.BirthDateError.NONE);
    private final MutableLiveData<Boolean> editBirthDateSaveEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<ProfileResult.BirthDateError> birthDateError = new MutableLiveData<>(ProfileResult.BirthDateError.NONE);
    private final MutableLiveData<ProfileResult.EditOptions> editOption = new MutableLiveData<>(ProfileResult.EditOptions.NONE);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final ProfileRepository profileRepository = new ProfileRepository(this);

    public void viewProfile() {
        profileRepository.viewProfile();
    }



    /*-----------------------------------   popup Methods   ----------------------------------*/

    public void birthdayUpdate() {
        profileRepository.checkBirthDate(birthDate.getValue());
        onEditBirthDateFieldChange();
    }

    public void onEditBirthDateFieldChange() {
        editBirthDateSaveEnabled.setValue(editBirthDateError.getValue()
                == ProfileResult.BirthDateError.NONE);
    }

    public void setEditOption(ProfileResult.EditOptions editOption){
        this.editOption.setValue(editOption);
    }

    public void setEditOptionName(){
        setEditOption(ProfileResult.EditOptions.NAME);
    }

    public void setEditOptionPhoneNumber(){
        setEditOption(ProfileResult.EditOptions.PHONE);
    }

    public void setEditOptionBirthDate(){
        setEditOption(ProfileResult.EditOptions.BIRTH_DATE);
    }

    /*-----------------------------------   Interface Methods   ----------------------------------*/

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
        this.name.setValue(fullName);
    }

    @Override
    public void setBirthDateField(Date birthDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.birthDate.setValue(formatter.format(birthDate));
    }

    @Override
    public void setPhoneNumberField(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    @Override
    public void setBirthDateStatus(ProfileResult.BirthDateError birthDateError) {
        this.editBirthDateError.setValue(birthDateError);
    }

    @Override
    public void setLoadingFinished() {
        loading.setValue(false);
    }


    /*-------------------------------------   Data Getters   -------------------------------------*/

    public LiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getBirthDate() {
        return birthDate;
    }

    public MutableLiveData<String> getEditNameText() {
        return editNameText;
    }

    public MutableLiveData<String> getEditPhoneNumberText() {
        return editPhoneNumberText;
    }

    public MutableLiveData<String> getEditBirthDateText() {
        return editBirthDateText;
    }

    public MutableLiveData<ProfileResult.BirthDateError> getEditBirthDateError() {
        return editBirthDateError;
    }

    public LiveData<Boolean> getEditBirthDateSaveEnabled() {
        return editBirthDateSaveEnabled;
    }

    public MutableLiveData<ProfileResult.BirthDateError> getBirthDateError() {
        return birthDateError;
    }

    public MutableLiveData<ProfileResult.EditOptions> getEditOption(){
        return editOption;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

}
