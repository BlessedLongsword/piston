package com.example.piston.main.profile.image;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileImageViewModel extends ViewModel implements ProfileImageRepository.IProfileImage {

    private final MutableLiveData<String> imageLink = new MutableLiveData<>("");

    private final ProfileImageRepository repository;

    public ProfileImageViewModel(String email) {
        repository = new ProfileImageRepository(this, email);
    }

    public void setImage(Uri image) {
        repository.setImage(image);
    }

    @Override
    public void setImageLink(String imageLink) {
        this.imageLink.setValue(imageLink);
    }

    public LiveData<String> getImageLink() {
        return imageLink;
    }
}
