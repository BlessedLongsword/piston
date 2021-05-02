package com.example.piston.main.groups.group.info;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityGroupInfoBinding;
import com.example.piston.utilities.MyViewModelFactory;

public class GroupInfoActivity extends AppCompatActivity {

    GroupInfoViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        Intent intent = getIntent();
        String title = intent.getStringExtra("document");

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(title))
                .get(GroupInfoViewModel.class);
        ActivityGroupInfoBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_group_info);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.groupInfoTopAppBar.setNavigationOnClickListener((view) -> finish());

        viewModel.getTitle().observe(this, binding.groupInfoTopAppBar::setTitle);
        viewModel.getImageLink().observe(this, aString -> Glide.with(this)
            .load(aString)
            .into(binding.groupInfoImage));
    }

}
