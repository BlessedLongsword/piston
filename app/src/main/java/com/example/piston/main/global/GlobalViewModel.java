package com.example.piston.main.global;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Category;

import java.util.ArrayList;

public class GlobalViewModel extends ViewModel implements GlobalRepository.IGlobal {

    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>(new ArrayList<>());
    private final GlobalRepository globalRepository = new GlobalRepository(this);

    public LiveData<ArrayList<Category>> getCategories() {
        return categories;
    }

    @Override
    public void setCategories(ArrayList<Category> categories) {
        this.categories.setValue(categories);
    }

    public void removeListener() {
        globalRepository.removeListener();
    }
}