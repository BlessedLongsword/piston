package com.example.piston.main.notifications;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

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

    public void test(MenuItem menuItem){
        //if (mActionMode == null)
            //mActionMode = startSupportActionMode((androidx.appcompat.view.ActionMode.Callback) mActionModeCallback);
    }

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.notifications_top_app_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            /*switch (item.getItemId()){
                case R.id.notif_read:
                    Toast.makeText(NotificationsActivity.this, "Mark notifications as read", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.notif_delete:
                    Toast.makeText(NotificationsActivity.this, "Delete notifications", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    return false;
            }*/
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            androidx.appcompat.view.ActionMode mActionMode = null;
        }
    };

}
