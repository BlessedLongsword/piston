package com.example.piston.main.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.notifications.Notification;

import java.util.ArrayList;

public class NotificationsViewModel extends ViewModel implements NotificationsRepository.INotifications {

    private final MutableLiveData<ArrayList<Notification>> notifications = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Notification>> newNotifications = new MutableLiveData<>();

    private final NotificationsRepository repository;

    public NotificationsViewModel() {
        repository = new NotificationsRepository(this);
    }

    @Override
    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications.setValue(notifications);
    }

    @Override
    public void setNewNotifications(ArrayList<Notification> notifications) {
        this.newNotifications.setValue(notifications);
    }

    @Override
    protected void onCleared () {
        repository.removeListener();
    }

    public LiveData<ArrayList<Notification>> getNotifications() {
        return notifications;
    }
    public LiveData<ArrayList<Notification>> getNewNotifications() {
        return newNotifications;
    }
}
