package com.example.piston.main.notifications;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.google.android.material.appbar.MaterialToolbar;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        MaterialToolbar bar = findViewById(R.id.notifications_topAppBar);
        bar.setNavigationOnClickListener((view) -> finish());
    }

}
