package com.example.piston.main.personal.folder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityFolderBinding;
import com.example.piston.main.personal.folder.info.FolderInfoActivity;
import com.example.piston.main.posts.createPost.CreatePostActivity;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.Values;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FolderActivity extends AppCompatActivity {

    private String folderID;
    private FolderViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Intent intent = getIntent();
        folderID = intent.getStringExtra(Values.SECTION_ID);

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(folderID))
                .get(FolderViewModel.class);
        ActivityFolderBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_folder);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.folderTopAppBar.setNavigationOnClickListener((view) -> finish());

        binding.recyclerviewFolder.setAdapter(new FolderAdapter(this));

        viewModel.getTitle().observe(this, binding.folderTopAppBar::setTitle);
        viewModel.getFilter().observe(this, s -> {
            if (Values.FILTER_ALPHABETICALLY.equals(s)) {
                binding.filterFieldText.setText(R.string.filter_alphabetically);
            } else {
                binding.filterFieldText.setText(R.string.filter_default);
            }
        });
        binding.filterField.setOnClickListener(chooseFilter());
    }

    @SuppressWarnings("unused")
    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra(Values.SCOPE, Values.PERSONAL);
        intent.putExtra(Values.SECTION_ID, folderID);
        startActivity(intent);
    }

    private View.OnClickListener chooseFilter() {
        return v -> new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.filter_by))
                .setItems(R.array.folder_filters, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            updateFilter(Values.FILTER_DEFAULT);
                            break;
                        case 1:
                            updateFilter(Values.FILTER_ALPHABETICALLY);
                            break;
                    }
                }).show();
    }

    private void updateFilter(String filter) {
        viewModel.updateFilter(filter, false);
    }

    @SuppressWarnings("unused")
    public void goToInfo(View view) {
        goToInfo();
    }

    @SuppressWarnings("unused")
    public void goToInfo(MenuItem item) {
        goToInfo();
    }

    private void goToInfo() {
        Intent intent = new Intent(this, FolderInfoActivity.class);
        intent.putExtra(Values.SECTION_ID, folderID);
        startActivityForResult(intent, Values.DELETE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Values.DELETE_CODE)
            finish();
    }

}
