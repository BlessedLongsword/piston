package com.example.piston.main.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Section;

import java.util.ArrayList;

public class NotificationViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Section>> notifications;
    private final ArrayList<Section> notification_array;


    public NotificationViewModel() {
        notification_array = new ArrayList<>();
        notifications = new MutableLiveData<>(notification_array);

    }

    public LiveData<ArrayList<Section>> getNotifications() {
        return notifications;
    }

    public void addNotification(String title, String text){

        notification_array.add(new Section(title, text));
        notifications.setValue(notification_array);
    }
}
