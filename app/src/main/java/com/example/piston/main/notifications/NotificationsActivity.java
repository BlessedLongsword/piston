package com.example.piston.main.notifications;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityNotificationsBinding;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        NotificationsViewModel viewModel = new ViewModelProvider(this)
                .get(NotificationsViewModel.class);
        ActivityNotificationsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_notifications);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.notificationsTopAppBar.setNavigationOnClickListener((view) -> finish());
        binding.recyclerviewNotifications.setAdapter(new NotificationsAdapter(this));
    }
}
