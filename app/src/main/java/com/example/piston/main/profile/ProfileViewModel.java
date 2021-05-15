package com.example.piston.main.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.authentication.register.RegisterResult;
import com.example.piston.data.Post;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileViewModel extends ViewModel implements ProfileRepository.IProfile{

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> name = new MutableLiveData<>("");
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> birthDate = new MutableLiveData<>("");
    private final MutableLiveData<String> imageLink = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isCurrentUser = new MutableLiveData<>(false);
    private final MutableLiveData<Post> featuredPost = new MutableLiveData<>(null);

    /*PupUps*/
    private final MutableLiveData<Boolean> editBirthDateSaveEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<RegisterResult.BirthDateError> birthDateError = new MutableLiveData<>(RegisterResult.BirthDateError.NONE);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finished = new MutableLiveData<>(false);

    private final ProfileRepository profileRepository;

    public ProfileViewModel(String email) {
        profileRepository = new ProfileRepository(this, email);
    }

    public void loadProfile() {
        profileRepository.loadProfile();
    }

    public void resetValues() {
        editBirthDateSaveEnabled.setValue(false);
        birthDateError.setValue(RegisterResult.BirthDateError.NONE);
        loading.setValue(false);
        finished.setValue(false);
    }


    /*-----------------------------------   popup Methods   ----------------------------------*/

    public void birthdayUpdate(String birth) {
        profileRepository.checkBirthDate(birth);
        onEditBirthDateFieldChange();
    }

    public void onEditBirthDateFieldChange() {
        editBirthDateSaveEnabled.setValue(birthDateError.getValue() == RegisterResult.BirthDateError.NONE);
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
    public void setImageLink(String imageLink) {
        this.imageLink.setValue(imageLink);
    }

    @Override
    public void setBirthDateStatus(RegisterResult.BirthDateError birthDateError) {
        this.birthDateError.setValue(birthDateError);
    }

    @Override
    public void setLoadingFinished() {
        loading.setValue(false);
        finished.setValue(true);
    }

    @Override
    public void setIsCurrentUser(boolean isCurrentUser) {
        this.isCurrentUser.setValue(isCurrentUser);
    }

    @Override
    public void setFeaturedPost(Post featuredPost) {
        this.featuredPost.setValue(featuredPost);
    }


    /*-------------------------------------   Data Getters   -------------------------------------*/

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getBirthDate() {
        return birthDate;
    }

    public LiveData<Boolean> getEditBirthDateSaveEnabled() {
        return editBirthDateSaveEnabled;
    }

    public LiveData<RegisterResult.BirthDateError> getBirthDateError() {
        return birthDateError;
    }

    public MutableLiveData<Boolean> getFinished() {
        return finished;
    }

    public LiveData<String> getImageLink() {
        return imageLink;
    }

    public LiveData<Post> getFeaturedPost() { return featuredPost; }

    public void editName(String text) {
        profileRepository.editName(text);
    }

    public void editPhone(String text) {
        profileRepository.editPhone(text);
    }

    public void editBirth(String text) {
        profileRepository.editBirth(text);
    }

    public LiveData<Boolean> getIsCurrentUser() {
        return isCurrentUser;
    }

}
