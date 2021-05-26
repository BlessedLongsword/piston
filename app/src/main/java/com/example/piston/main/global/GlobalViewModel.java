package com.example.piston.main.global;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.sections.Category;
import com.example.piston.utilities.Values;

import java.util.ArrayList;

public class GlobalViewModel extends ViewModel implements GlobalRepository.IGlobal {

    private final MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<ArrayList<Boolean>> subscribed = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isAdmin = new MutableLiveData<>(false);
    private final MutableLiveData<String> filter = new MutableLiveData<>(Values.FILTER_DEFAULT);
    private final GlobalRepository globalRepository;

    public GlobalViewModel (boolean nsfw) {
        globalRepository = new GlobalRepository(this, nsfw);
    }

    public LiveData<ArrayList<Category>> getCategories() {
        return categories;
    }

    public void updateFilter(String filter, boolean descending, boolean nsfw) {
        globalRepository.updateQuery(filter, descending, nsfw);
    }

    @Override
    public void setCategories(ArrayList<Category> categories) {
        Log.d("DBReadTAG", "Cats: " + categories.size());
        this.categories.setValue(categories);
    }

    @Override
    public void setSubscribed(ArrayList<Boolean> subscriptions){
        Log.d("DBReadTAG", "Subs: " + subscriptions.size());
        this.subscribed.setValue(subscriptions);
    }

    @Override
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin.setValue(isAdmin);
    }

    @Override
    public void setFilter(String filter) {
        this.filter.setValue(filter);
    }

    public void setSub(boolean sub, String title){
        globalRepository.addSub(sub,title);
    }

    public LiveData<ArrayList<Boolean>> getSubscribed(){
        return this.subscribed;
    }

    public LiveData<Boolean> getIsAdmin() {
        return isAdmin;
    }

    public void removeListener() {
        globalRepository.removeListener();
    }

    public LiveData<String> getFilter() {
        return filter;
    }

    public void showNsfw(boolean nsfwVisiblity) {
        globalRepository.showNsfw(nsfwVisiblity);
    }
}
