package com.example.piston.main.groups.createGroup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreateGroupBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CreateGroupActivity extends AppCompatActivity {

    CreateGroupViewModel createGroupViewModel;
    ClipboardManager clipboard;
    ClipData clip;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_group);

        createGroupViewModel = new ViewModelProvider(this).get(CreateGroupViewModel.class);
        ActivityCreateGroupBinding  binding = DataBindingUtil.setContentView(this, R.layout.activity_create_group);
        binding.setViewModel(createGroupViewModel);
        binding.setLifecycleOwner(this);

        createGroupViewModel.generateGroupID();

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        binding.groupLink.setEndIconOnClickListener(v -> {
            clip = ClipData.newPlainText("GroupCode", binding.groupLink.getEditText().getText());
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

    }

    public void createGroup(MenuItem item) {
        createGroupViewModel.createGroup();
    }
}