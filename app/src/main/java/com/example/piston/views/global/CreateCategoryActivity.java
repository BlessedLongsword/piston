package com.example.piston.views.global;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreateCategoryBinding;
import com.example.piston.databinding.ActivityRegisterBinding;
import com.example.piston.databinding.ActivityRegisterBindingImpl;
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.example.piston.viewmodels.CreateCategoryViewModel;
import com.example.piston.viewmodels.GlobalFragmentViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

public class CreateCategoryActivity extends AppCompatActivity {

    CreateCategoryViewModel createCategoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_category);
        createCategoryViewModel = new ViewModelProvider(this).get(CreateCategoryViewModel.class);
        ActivityCreateCategoryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_category);
        binding.setViewModel(createCategoryViewModel);
        binding.setLifecycleOwner(this);
        binding.createCategoryTopAppBar.setNavigationOnClickListener(v -> finish());
        createCategoryViewModel.getCreateError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_category_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> {

                        })
                        .show();
            }
        });
        createCategoryViewModel.getFinishCreateCategory().observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });
    }

    public void createCategory(MenuItem item) {
        createCategoryViewModel.createCategory();
    }


}
