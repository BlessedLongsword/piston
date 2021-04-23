package com.example.piston.utilities;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.main.global.category.CategoryViewModel;
import com.example.piston.main.personal.folder.FolderViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    private final String mParam;

    public MyViewModelFactory(String param) {
        mParam = param;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class))
            return (T) new CategoryViewModel(mParam);
        if (modelClass.isAssignableFrom(FolderViewModel.class))
            return (T) new FolderViewModel(mParam);
        throw new IllegalArgumentException("Unable to construct viewmodel");
    }

}