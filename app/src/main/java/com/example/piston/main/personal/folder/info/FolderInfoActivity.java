package com.example.piston.main.personal.folder.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityFolderInfoBinding;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.Values;

public class FolderInfoActivity extends AppCompatActivity {

    FolderInfoViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_info);

        Intent intent = getIntent();
        String folderID = intent.getStringExtra(Values.SECTION_ID);

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(folderID))
                .get(FolderInfoViewModel.class);
        ActivityFolderInfoBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_folder_info);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.folderInfoTopAppBar.setNavigationOnClickListener((view) -> finish());

        viewModel.getTitle().observe(this, binding.folderInfoTopAppBar::setTitle);
    }

    @SuppressWarnings("unused")
    public void deleteFolder(MenuItem item) {
        viewModel.deleteFolder();
        setResult(Values.DELETE_CODE);
        finish();
    }
}
