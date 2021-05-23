package com.example.piston.main.global.category.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CategoryInfoViewModel extends ViewModel implements CategoryInfoRepository.ICategoryInfo {

    private final MutableLiveData<String> description = new MutableLiveData<>("");
    private final MutableLiveData<String> imageLink = new MutableLiveData<>("");
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> subscribed = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isAdmin = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> finished = new MutableLiveData<>(false);

    private final CategoryInfoRepository repository;

    public CategoryInfoViewModel(String category) {
        repository = new CategoryInfoRepository(this, category);
        repository.checkSub();
    }

    @Override
    public void setParams(String title, String description, String imageLink) {
        this.title.setValue(title);
        this.description.setValue(description);
        this.imageLink.setValue(imageLink);
    }

    @Override
    public void setSubscribed(boolean subscribed){
        this.subscribed.setValue(subscribed);
    }

    @Override
    public void setIsAdmin(boolean admin) {
        this.isAdmin.setValue(admin);
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

    public LiveData<Boolean> getIsAdmin() {
        return isAdmin;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public void deleteCategory() {
        repository.deleteCategory();
    }

    public void editDescription(String text) {
        repository.editDescription(text);
    }

    public void setFinished() {
        finished.setValue(true);
    }

    public void update() {
        repository.updateParams();
    }

    public LiveData<Boolean> getFinished() {
        return finished;
    }

    public void editTitle(String text) {
        repository.editTitle(text);
    }

    public void reset() {
        finished.setValue(false);
    }

}
