package com.example.piston.main.global;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GlobalViewModel extends ViewModel implements GlobalRepository.IGlobal {

    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<HashMap<Integer, Boolean>> subscribed = new MutableLiveData<>(new HashMap<>());
    private final GlobalRepository globalRepository = new GlobalRepository(this);

    public LiveData<ArrayList<Category>> getCategories() {
        return categories;
    }


    @Override
    public void setCategories(ArrayList<Category> categories) {
        this.categories.setValue(categories);
    }

    @Override
    public void setSubscribed(HashMap<Integer, Boolean> subs){
        this.subscribed.setValue(subs);
    }
    public void setSub(boolean sub, String title){
        globalRepository.addSub(sub,title);
    }

    public LiveData<HashMap<Integer, Boolean>> getSubscribed(){
        return this.subscribed;
    }


    public void removeListener() {
        globalRepository.removeListener();
    }
}
