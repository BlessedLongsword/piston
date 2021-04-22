package com.example.piston.utilities;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.main.global.category.CategoryViewModel;

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
        throw new IllegalArgumentException("Unable to construct viewmodel");
    }
}