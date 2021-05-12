package com.example.piston.main.personal.folder.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.main.personal.folder.info.FolderInfoRepository;

public class FolderInfoViewModel extends ViewModel implements FolderInfoRepository.IFolderInfo {

    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> description = new MutableLiveData<>("");

    final FolderInfoRepository repository;

    public FolderInfoViewModel(String folder) {
        repository = new FolderInfoRepository(this, folder);
    }

    @Override
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    @Override
    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public LiveData<String> getTitle() { return title; }

    public LiveData<String> getDescription() {
        return description;
    }

    public void deleteFolder() {
        repository.deleteFolder();
    }

}
