package com.example.piston.utilities;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.authentication.googleRegister.GoogleRegisterViewModel;
import com.example.piston.main.global.category.CategoryViewModel;
import com.example.piston.main.global.category.info.CategoryInfoViewModel;
import com.example.piston.main.groups.group.GroupViewModel;
import com.example.piston.main.groups.group.info.GroupInfoViewModel;
import com.example.piston.main.personal.folder.FolderViewModel;
import com.example.piston.main.personal.folder.info.FolderInfoViewModel;
import com.example.piston.main.posts.PostViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    private final String mParam;
    private final String mParam1;
    private final String mParam2;

    public MyViewModelFactory(String param) {
        mParam = param;
        mParam1 = null;
        mParam2 = null;
    }

    public MyViewModelFactory(String param, String param1, String param2) {
        mParam = param;
        mParam1 = param1;
        mParam2 = param2;
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
        if (modelClass.isAssignableFrom(FolderInfoViewModel.class))
            return (T) new FolderInfoViewModel(mParam);
        if (modelClass.isAssignableFrom(GroupInfoViewModel.class))
            return (T) new GroupInfoViewModel(mParam);
        if (modelClass.isAssignableFrom(CategoryInfoViewModel.class))
            return (T) new CategoryInfoViewModel(mParam);
        if (modelClass.isAssignableFrom(GoogleRegisterViewModel.class))
            return (T) new GoogleRegisterViewModel(mParam);
        if (modelClass.isAssignableFrom(PostViewModel.class))
            return (T) new PostViewModel(mParam, mParam1, mParam2);
        throw new IllegalArgumentException("Unable to construct ViewModel");
    }

}