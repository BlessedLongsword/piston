package com.example.piston.main.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityNotificationsBinding;
import com.example.piston.utilities.Values;


public class NotificationsActivity extends AppCompatActivity {

    private NotificationsAdapter adapter;
    ActionMode actionMode;
    ActionCallback actionCallback;
    boolean darkModeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        actionCallback = new ActionCallback();

        SharedPreferences prefs = getSharedPreferences(Values.SHARED_PREFS, Context.MODE_PRIVATE);
        boolean followSystem = false;

        if (Build.VERSION.SDK_INT >= 29)
            followSystem = prefs.getBoolean(Values.THEME_FOLLOW_SYSTEM, false);

        if (followSystem) {
            int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    darkModeEnabled = true;
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    darkModeEnabled = false;
                    break;
            }
        }

        adapter = new NotificationsAdapter(this, darkModeEnabled);
        NotificationsViewModel viewModel = new ViewModelProvider(this)
                .get(NotificationsViewModel.class);
        ActivityNotificationsBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_notifications);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.notificationsTopAppBar.setNavigationOnClickListener((view) -> finish());
        binding.recyclerviewNotifications.setAdapter(adapter);
        adapter.setItemClick((view, notification, position) -> toggleActionBar(position));
        viewModel.getNotifications().observe(this, notifications -> {
            if (notifications.size() > 0)
                binding.noNotifications.setVisibility(View.GONE);
            else
                binding.noNotifications.setVisibility(View.VISIBLE);
        });
    }

    private void toggleActionBar(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.selectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionCallback implements  androidx.appcompat.view.ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            NotificationMultiSelection.toggleStatusBarColor(NotificationsActivity.this,
                    (darkModeEnabled) ? R.color.design_default_color_secondary_variant : R.color.black);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            return false;
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.notification_delete) {
                adapter.deleteNotifications();
                mode.finish();
                return true;
            } else if (itemId == R.id.notification_read) {
                adapter.markAsRead();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
            NotificationMultiSelection.toggleStatusBarColor(NotificationsActivity.this,
                    (darkModeEnabled) ? R.color.design_default_color_primary_dark : R.color.black);
        }
    }
}
