package com.example.piston.main.posts.createPost;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;



import java.util.Objects;

public class CreatePostViewModel extends ViewModel implements CreatePostRepository.ICreatePost{
    
    private final MutableLiveData<String> titleField = new MutableLiveData<>("");
    private final MutableLiveData<String> contentField = new MutableLiveData<>("");
    private final MutableLiveData<CreatePostResult.TitleError> titleFieldError =
            new MutableLiveData<>(CreatePostResult.TitleError.NONE);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> createError = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finishCreatePost = new MutableLiveData<>(false);

    private final CreatePostRepository repository = new CreatePostRepository(this);

    public void generatePostID() {
        repository.generatePostID();
        loading.setValue(true);
    }

    public void titleUpdate() {
        repository.checkTitle(Objects.requireNonNull(titleField.getValue()));
        createError.setValue(false);
    }

    public void createPost(String collection, String document) {
        repository.createPost(collection, document, Objects.requireNonNull(titleField.getValue())
                , contentField.getValue());
        loading.setValue(true);
    }

    @Override
    public void setTitleStatus(CreatePostResult.TitleError titleError) {
        titleFieldError.setValue(titleError);
    }

    @Override
    public void setCreateError() {
        createError.setValue(true);
    }

    @Override
    public void setCreateFinished() {
        finishCreatePost.setValue(true);
    }

    @Override
    public void setLoadingFinished() {
        loading.setValue(false);
    }

    public MutableLiveData<String> getTitleField() {
        return titleField;
    }

    public MutableLiveData<String> getContentField() {
        return contentField;
    }

    public LiveData<CreatePostResult.TitleError> getTitleFieldError() {
        return titleFieldError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getCreateError() {
        return createError;
    }

    public LiveData<Boolean> getFinishCreatePost() {
        return finishCreatePost;
    }

    public void uploadImage(byte[] image) {
        repository.uploadImage(image);
    }
}
