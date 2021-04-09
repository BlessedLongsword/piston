package com.example.piston.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.Section;

import java.util.ArrayList;

public class GlobalFragmentViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Section>> categories;
    private final ArrayList<Section> categories_array;
    //private final CategoryManager categoryManager;

    public GlobalFragmentViewModel() {
        categories_array = new ArrayList<>();
        categories = new MutableLiveData<>(categories_array);
        //categoryManager = new CategoryManager();
    }

    public LiveData<ArrayList<Section>> getCategories() {
        return categories;
    }

    public void createCategory(String title, String description){
        //categoryManager.createFolder(title, description);
        //folders.postValue(categoryManager.getFolderNames());
        categories_array.add(new Section(title, description));
        categories.setValue(categories_array);
    }

}
