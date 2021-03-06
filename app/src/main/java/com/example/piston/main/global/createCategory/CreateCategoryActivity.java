package com.example.piston.main.global.createCategory;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreateCategoryBinding;
import com.example.piston.utilities.PickImageActivity;
import com.example.piston.utilities.CheckNetwork;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class CreateCategoryActivity extends PickImageActivity {

    private ActivityCreateCategoryBinding binding;
    private CreateCategoryViewModel createCategoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_category);
        createCategoryViewModel = new ViewModelProvider(this).get(CreateCategoryViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_category);
        binding.setViewModel(createCategoryViewModel);
        binding.setLifecycleOwner(this);
        binding.createCategoryTopAppBar.setNavigationOnClickListener(v -> finish());
        createCategoryViewModel.getCreateError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_category_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> { })
                        .show();
            }
        });
        createCategoryViewModel.getImageError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_category_image_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> { })
                        .show();
            }
        });
        createCategoryViewModel.getFinishCreateCategory().observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });
    }

    @SuppressWarnings("unused")
    public void createCategory(MenuItem item) {
        boolean connected = CheckNetwork.isConnected(getApplicationContext());
        if (!Objects.requireNonNull(createCategoryViewModel.getLoading().getValue()))
            createCategoryViewModel.createCategory(imageUri, connected);
    }

    @Override
    protected void setUri(Uri imageUri) {
        binding.categoryPicture.setImageURI(imageUri);
    }
}
