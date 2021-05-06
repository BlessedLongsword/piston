package com.example.piston.main.groups.createGroup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreateGroupBinding;
import com.example.piston.main.sections.PickImageActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;


public class CreateGroupActivity extends PickImageActivity {

    private ActivityCreateGroupBinding binding;
    private CreateGroupViewModel createGroupViewModel;
    private ClipboardManager clipboard;
    private ClipData clip;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_group);

        createGroupViewModel = new ViewModelProvider(this).get(CreateGroupViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_group);
        binding.setViewModel(createGroupViewModel);
        binding.setLifecycleOwner(this);

        createGroupViewModel.generateGroupID();

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        binding.groupLink.setEndIconOnClickListener(v -> {
            clip = ClipData.newPlainText("GroupCode", Objects.requireNonNull(binding.groupLink.getEditText()).getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.link_copied, Toast.LENGTH_LONG).show();
        });

        binding.createGroupTopAppBar.setNavigationOnClickListener(v -> finish());

        createGroupViewModel.getCreateError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_group_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> {
                        })
                        .show();
            }
        });
        createGroupViewModel.getFinishCreateGroup().observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });

        createGroupViewModel.getErrorMessage().observe(this, message -> {
            if (message != null)
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        });

    }

    public void createGroup(MenuItem item) {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        createGroupViewModel.createGroup(image, connected);
    }

    @Override
    protected void setImage(Bitmap bitmap) {
        binding.groupPicture.setImageBitmap(bitmap);
    }

}
