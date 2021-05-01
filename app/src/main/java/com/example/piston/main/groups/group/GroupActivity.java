package com.example.piston.main.groups.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityFolderBinding;
import com.example.piston.databinding.ActivityGroupBinding;
import com.example.piston.main.posts.createPost.CreatePostActivity;
import com.example.piston.utilities.MyViewModelFactory;

public class GroupActivity extends AppCompatActivity {

    private String title, id;
    private GroupViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(id))
                .get(GroupViewModel.class);
        ActivityGroupBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_group);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.viewNotesTopAppBar.setTitle(title);

        binding.recyclerviewGroups.setAdapter(new GroupAdapter(this));
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("collection", "groups");
        intent.putExtra("document", id);
        startActivity(intent);
    }
}
