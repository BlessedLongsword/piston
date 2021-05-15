package com.example.piston.main.groups.createGroup;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class CreateGroupViewModel extends ViewModel implements CreateGroupRepository.ICreateGroup {

    private final MutableLiveData<String> titleField = new MutableLiveData<>("");
    private final MutableLiveData<String> descriptionField = new MutableLiveData<>("");
    private final MutableLiveData<CreateGroupResult.TitleError> titleFieldError =
            new MutableLiveData<>(CreateGroupResult.TitleError.NONE);
    private final MutableLiveData<String> groupIDField = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> createError = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finishCreateGroup = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final CreateGroupRepository repository = new CreateGroupRepository(this);

    public void generateGroupID() {
        repository.generateGroupID();
        loading.setValue(true);
    }

    public void titleUpdate() {
        repository.checkTitle(Objects.requireNonNull(titleField.getValue()));
        createError.setValue(false);
    }

    public void createGroup(Uri image, boolean connected) {
        loading.setValue(true);
        repository.createGroup(Objects.requireNonNull(titleField.getValue()),
                descriptionField.getValue(), groupIDField.getValue(), image, connected);
    }

    @Override
    public void setGroupID(String groupID) {
        groupIDField.setValue(groupID);
    }

    @Override
    public void setTitleStatus(CreateGroupResult.TitleError error) {
        titleFieldError.setValue(error);
    }

    @Override
    public void setCreateError() {
        createError.setValue(true);
    }

    @Override
    public void setCreateFinished() {
        finishCreateGroup.setValue(true);
    }

    @Override
    public void setLoadingFinished() {
        loading.setValue(false);
    }

    @Override
    public void setErrorMessage(String message) {
        errorMessage.setValue(message);
    }

    public MutableLiveData<String> getTitleField() {
        return titleField;
    }

    public MutableLiveData<String> getDescriptionField() {
        return descriptionField;
    }

    public LiveData<String> getGroupIDField() {
        return groupIDField;
    }

    public LiveData<CreateGroupResult.TitleError> getTitleFieldError() {
        return titleFieldError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getCreateError() {
        return createError;
    }

    public LiveData<Boolean> getFinishCreateGroup() {
        return finishCreateGroup;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
