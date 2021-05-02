package com.example.piston.main.global.createCategory;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreateCategoryBinding;
import com.example.piston.main.sections.PickImageActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

    public void createCategory(MenuItem item) {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        createCategoryViewModel.createCategory(image, connected);
    }

    @Override
    protected void setImage(Bitmap bitmap) {
        binding.categoryPicture.setImageBitmap(bitmap);
    }
}
