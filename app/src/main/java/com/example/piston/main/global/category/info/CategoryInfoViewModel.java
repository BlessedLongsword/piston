package com.example.piston.main.global.category.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CategoryInfoViewModel extends ViewModel implements CategoryInfoRepository.ICategoryInfo {

    private final MutableLiveData<String> description = new MutableLiveData<>("");
    private final MutableLiveData<String> imageLink = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> subscribed = new MutableLiveData<>(false);

    final CategoryInfoRepository repository;

    public CategoryInfoViewModel(String category) {
        repository = new CategoryInfoRepository(this, category);
        repository.checkSub();
    }

    @Override
    public void setDescription(String description) {
        this.description.setValue(description);
    }

    @Override
    public void setImageLink(String imageLink) {
        this.imageLink.setValue(imageLink);
    }

    @Override
    public void setSubscribed(boolean subscribed){
        this.subscribed.setValue(subscribed);
    }

    public void setSub(boolean sub){
        this.subscribed.setValue(sub);
        repository.addSub(sub);
    }

    public LiveData<String> getDescription() {
        return description;
    }

    public LiveData<String> getImageLink() {
        return imageLink;
    }

    public LiveData<Boolean> getSubscribed() {return subscribed;}
}
