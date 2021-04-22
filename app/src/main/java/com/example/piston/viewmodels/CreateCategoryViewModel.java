package com.example.piston.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.CreateCategoryResult;
import com.example.piston.data.repositories.CreateCategoryRepository;

import java.util.Objects;

public class CreateCategoryViewModel extends ViewModel implements CreateCategoryRepository.ICreateCategory {

    private final MutableLiveData<String> titleField = new MutableLiveData<>("");
    private final MutableLiveData<String> descriptionField = new MutableLiveData<>("");
    private final MutableLiveData<CreateCategoryResult.TitleError> titleFieldError =
            new MutableLiveData<>(CreateCategoryResult.TitleError.NONE);
    private final MutableLiveData<Boolean> nsfwBox = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> createError = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finishCreateCategory = new MutableLiveData<>(false);


    private final CreateCategoryRepository repository = new CreateCategoryRepository(this);

    public void titleUpdate() {
        repository.checkTitle(Objects.requireNonNull(titleField.getValue()));
        createError.setValue(false);
    }

    public void createCategory() {
        repository.createCategory(Objects.requireNonNull(titleField.getValue())
                , descriptionField.getValue(), nsfwBox.getValue());
        loading.setValue(true);
    }

    @Override
    public void setTitleStatus(CreateCategoryResult.TitleError titleError) {
        titleFieldError.setValue(titleError);
    }

    @Override
    public void setCreateError() {
        createError.setValue(true);
    }

    @Override
    public void setCreateFinished() {
        finishCreateCategory.setValue(true);
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

    public LiveData<CreateCategoryResult.TitleError> getTitleFieldError() {
        return titleFieldError;
    }

    public MutableLiveData<Boolean> getNsfwBox() {
        return nsfwBox;
    }

    public LiveData<Boolean> getCreateError() {
        return createError;
    }

    public LiveData<Boolean> getFinishCreateCategory() {
        return finishCreateCategory;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

}
