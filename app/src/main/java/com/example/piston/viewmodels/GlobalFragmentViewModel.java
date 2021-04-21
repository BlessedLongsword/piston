package com.example.piston.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Category;
import com.example.piston.data.repositories.GlobalRepository;

import java.util.ArrayList;

public class GlobalFragmentViewModel extends ViewModel implements GlobalRepository.IGlobal {

    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>(new ArrayList<>());

    public GlobalFragmentViewModel() {
        GlobalRepository globalRepository = new GlobalRepository(this);
        globalRepository.loadCategories();
    }

    public LiveData<ArrayList<Category>> getCategories() {
        return categories;
    }


    @Override
    public void setCategories(ArrayList<Category> categories) {
        this.categories.setValue(categories);
    }
}
