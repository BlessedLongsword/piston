package com.example.piston.main.global.category.info;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityCategoryInfoBinding;
import com.example.piston.utilities.MyViewModelFactory;

public class CategoryInfoActivity extends AppCompatActivity {

    private String title;
    CategoryInfoViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_info);

        Intent intent = getIntent();
        title = intent.getStringExtra("document");

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(title))
                .get(CategoryInfoViewModel.class);
        ActivityCategoryInfoBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_category_info);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.categoryInfoTopAppBar.setTitle(title);
        binding.categoryInfoTopAppBar.setNavigationOnClickListener((view) -> finish());

        viewModel.getImageLink().observe(this, aString -> {
            Glide.with(this)
                    .load(aString)
                    .into(binding.categoryInfoImage);
        });
    }

}
