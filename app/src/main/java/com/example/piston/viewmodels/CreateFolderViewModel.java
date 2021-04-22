package com.example.piston.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.CreateFolderResult;
import com.example.piston.data.Post;
import com.example.piston.data.repositories.CreateFolderRepository;
import com.example.piston.data.repositories.CreateFolderRepository;

import java.util.ArrayList;
import java.util.Objects;

public class CreateFolderViewModel extends ViewModel implements CreateFolderRepository.ICreateFolder {

    private final MutableLiveData<String> titleField = new MutableLiveData<>("");
    private final MutableLiveData<String> descriptionField = new MutableLiveData<>("");
    private final MutableLiveData<CreateFolderResult.TitleError> titleFieldError =
            new MutableLiveData<>(CreateFolderResult.TitleError.NONE);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> createError = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finishCreateFolder = new MutableLiveData<>(false);

    private final CreateFolderRepository repository = new CreateFolderRepository(this);
    
    public void titleUpdate() {
        repository.checkTitle(Objects.requireNonNull(titleField.getValue()));
        createError.setValue(false);
    }

    public void createFolder() {
        repository.createFolder(Objects.requireNonNull(titleField.getValue())
                , descriptionField.getValue());
        loading.setValue(true);
    }

    @Override
    public void setTitleStatus(CreateFolderResult.TitleError titleError) {
        titleFieldError.setValue(titleError);
    }

    @Override
    public void setCreateError() {
        createError.setValue(true);
    }

    @Override
    public void setCreateFinished() {
        finishCreateFolder.setValue(true);
    }

    @Override
    public void setLoadingFinished() {
        loading.setValue(false);
    }

    public MutableLiveData<String> getTitleField() {
        return titleField;
    }

    public MutableLiveData<String> getDescriptionField() {
        return descriptionField;
    }

    public LiveData<CreateFolderResult.TitleError> getTitleFieldError() {
        return titleFieldError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getCreateError() {
        return createError;
    }

    public LiveData<Boolean> getFinishCreateFolder() {
        return finishCreateFolder;
    }

}

