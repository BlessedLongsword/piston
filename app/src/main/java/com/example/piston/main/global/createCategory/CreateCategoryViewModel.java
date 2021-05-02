package com.example.piston.main.global.createCategory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class CreateCategoryViewModel extends ViewModel implements CreateCategoryRepository.ICreateCategory {

    private final MutableLiveData<String> titleField = new MutableLiveData<>("");
    private final MutableLiveData<String> descriptionField = new MutableLiveData<>("");
    private final MutableLiveData<CreateCategoryResult.TitleError> titleFieldError =
            new MutableLiveData<>(CreateCategoryResult.TitleError.NONE);
    private final MutableLiveData<Boolean> nsfwBox = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> createError = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> imageError = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finishCreateCategory = new MutableLiveData<>(false);

    private final CreateCategoryRepository repository = new CreateCategoryRepository(this);

    public void titleUpdate() {
        repository.checkTitle(Objects.requireNonNull(titleField.getValue()));
        createError.setValue(false);
    }

    public void createCategory(byte[] image, boolean connected) {
        loading.setValue(true);
        imageError.setValue(false);
        repository.createCategory(Objects.requireNonNull(titleField.getValue()),
                descriptionField.getValue(), Objects.requireNonNull(nsfwBox.getValue()), image, connected);
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

    @Override
    public void setImageError() {
        imageError.setValue(true);
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

    public LiveData<Boolean> getImageError() { return imageError; }

}
