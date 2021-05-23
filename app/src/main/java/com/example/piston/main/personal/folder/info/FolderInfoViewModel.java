package com.example.piston.main.personal.folder.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FolderInfoViewModel extends ViewModel implements FolderInfoRepository.IFolderInfo {

    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> description = new MutableLiveData<>("");

    private final MutableLiveData<Boolean> finished = new MutableLiveData<>(false);

    private final FolderInfoRepository repository;

    public FolderInfoViewModel(String folder) {
        repository = new FolderInfoRepository(this, folder);
    }

    @Override
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    @Override
    public void setFinished() {
        finished.setValue(true);
    }

    @Override
    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public void editDescription(String text) {
        repository.editDescription(text);
    }

    public LiveData<String> getTitle() { return title; }


    public LiveData<String> getDescription() {
        return description;
    }

    public void deleteFolder() {
        repository.deleteFolder();
    }

    public void reset() {
        finished.setValue(false);
    }

    public LiveData<Boolean> getFinished() {
        return finished;
    }

    public void update() {
        repository.updateParams();
    }

    public void editTitle(String text) {
        repository.editTitle(text);
    }
}
