package com.example.piston.main.personal.folder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityFolderBinding;
import com.example.piston.main.global.category.PostAdapter;
import com.example.piston.main.posts.createPost.CreatePostActivity;
import com.example.piston.utilities.MyViewModelFactory;

public class FolderActivity extends AppCompatActivity {

    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Intent intent = getIntent();
        title = intent.getStringExtra("id");

        FolderViewModel viewModel = new ViewModelProvider(this,
                new MyViewModelFactory(title)).get(FolderViewModel.class);
        ActivityFolderBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_folder);
        binding.setViewModel(viewModel);
         binding.setLifecycleOwner(this);

        binding.viewNotesTopAppBar.setTitle(title);

        binding.recyclerviewFolder.setAdapter(new NoteAdapter(this));
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("collection", "users");
        intent.putExtra("document", title);
        startActivity(intent);
    }
}
