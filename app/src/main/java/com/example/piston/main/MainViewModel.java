package com.example.piston.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel implements MainRepository.IMain {

    public final MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> fromShareBelongsToGroup =
            new MutableLiveData<>(null);

    private final MainRepository mainRepository = new MainRepository(this);

    public void logout() {
        mainRepository.logout();
    }

    @Override
    public void setSignedIn(boolean signedIn) {
        isSignedIn.setValue(signedIn);
    }

    @Override
    public void setFromShareBelongsToGroup(boolean belongsToGroup) {
        fromShareBelongsToGroup.setValue(belongsToGroup);
    }

    public void checkFromShareBelongsToGroup(String groupID) {
        mainRepository.checkFromShareBelongsToGroup(groupID);
    }

    public LiveData<Boolean> getFromShareBelongsToGroup() {
        return fromShareBelongsToGroup;
    }
}
