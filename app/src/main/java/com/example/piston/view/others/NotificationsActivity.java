package com.example.piston.view.others;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;

public class NotificationsActivity extends AppCompatActivity {
    private androidx.appcompat.view.ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

    }

    public void test(MenuItem menuItem){
        if (mActionMode == null){
            mActionMode = startSupportActionMode((androidx.appcompat.view.ActionMode.Callback) mActionModeCallback);
        }


    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
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
            switch (item.getItemId()){
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
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

}
