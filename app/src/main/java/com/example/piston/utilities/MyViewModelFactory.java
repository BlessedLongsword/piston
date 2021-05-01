package com.example.piston.utilities;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.authentication.googleRegister.GoogleRegisterViewModel;
import com.example.piston.main.global.category.CategoryViewModel;
import com.example.piston.main.global.category.info.CategoryInfoViewModel;
import com.example.piston.main.groups.group.GroupViewModel;
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
        if (modelClass.isAssignableFrom(GroupViewModel.class))
            return (T) new GroupViewModel(mParam);
        if (modelClass.isAssignableFrom(CategoryInfoViewModel.class))
            return (T) new CategoryInfoViewModel(mParam);
        if (modelClass.isAssignableFrom(GoogleRegisterViewModel.class))
            return (T) new GoogleRegisterViewModel(mParam);
        throw new IllegalArgumentException("Unable to construct viewmodel");
    }

}