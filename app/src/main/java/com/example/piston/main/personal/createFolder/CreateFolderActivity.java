package com.example.piston.main.personal.createFolder;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreateFolderBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class CreateFolderActivity extends AppCompatActivity {

    CreateFolderViewModel createFolderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);
        createFolderViewModel = new ViewModelProvider(this).get(CreateFolderViewModel.class);
        ActivityCreateFolderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_folder);
        binding.setViewModel(createFolderViewModel);
        binding.setLifecycleOwner(this);
        binding.createFolderTopAppBar.setNavigationOnClickListener(v -> finish());
        createFolderViewModel.getCreateError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_folder_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> { })
                        .show();
            }
        });
        createFolderViewModel.getFinishCreateFolder().observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });
    }

    @SuppressWarnings("unused")
    public void createFolder(MenuItem item) {
        if (!Objects.requireNonNull(createFolderViewModel.getLoading().getValue()))
            createFolderViewModel.createFolder();
    }
}
