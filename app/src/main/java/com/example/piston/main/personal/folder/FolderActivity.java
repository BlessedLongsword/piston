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

import static com.example.piston.utilities.Values.DELETE_CODE;

public class FolderActivity extends AppCompatActivity {

    private String folderID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Intent intent = getIntent();
        folderID = intent.getStringExtra(Values.SECTION_ID);

        FolderViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory(folderID))
                .get(FolderViewModel.class);
        ActivityFolderBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_folder);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.folderTopAppBar.setNavigationOnClickListener((view) -> finish());

        binding.recyclerviewFolder.setAdapter(new FolderAdapter(this));

        viewModel.getTitle().observe(this, binding.folderTopAppBar::setTitle);
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra(Values.SCOPE, Values.PERSONAL);
        intent.putExtra(Values.SECTION_ID, folderID);
        startActivity(intent);
    }


    public void goToInfo(View view) {
        goToInfo();
    }

    public void goToInfo(MenuItem item) {
        goToInfo();
    }

    private void goToInfo() {
        Intent intent = new Intent(this, FolderInfoActivity.class);
        intent.putExtra(Values.SECTION_ID, folderID);
        startActivityForResult(intent, DELETE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == DELETE_CODE)
            finish();
    }

}
