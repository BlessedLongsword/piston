package com.example.piston.main.notifications;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.data.notifications.Notification;
import com.example.piston.databinding.ActivityNotificationsBinding;


public class NotificationsActivity extends AppCompatActivity {

    private NotificationsAdapter adapter;
    ActionMode actionMode;
    ActionCallback actionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        actionCallback = new ActionCallback();
        adapter = new NotificationsAdapter(this);
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
            NotificationMultiSelection.toggleStatusBarColor(NotificationsActivity.this, R.color.black);
            mode.getMenuInflater().inflate(R.menu.notifications_top_app_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.notification_delete:
                    adapter.deleteNotifications();
                    mode.finish();
                    return true;
                case R.id.notification_read:
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
            NotificationMultiSelection.toggleStatusBarColor(NotificationsActivity.this, R.color.design_default_color_primary_variant);

        }
    }
}
