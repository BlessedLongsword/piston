package com.example.piston.main.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Notification;

import java.util.ArrayList;

public class NotificationsViewModel extends ViewModel implements NotificationsRepository.INotifications {

    private final MutableLiveData<ArrayList<Notification>> notifications = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Notification>> newNotifications = new MutableLiveData<>();

    public NotificationsViewModel() {
        NotificationsRepository repository = new NotificationsRepository(this);
    }

    @Override
    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications.setValue(notifications);
    }

    @Override
    public void setNewNotifications(ArrayList<Notification> notifications) {

    }

    public LiveData<ArrayList<Notification>> getNotifications() {
        return notifications;
    }
    public LiveData<ArrayList<Notification>> getNewNotifications() {
        return newNotifications;
    }
}
