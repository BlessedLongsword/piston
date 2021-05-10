package com.example.piston.main.groups.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityGroupBinding;
import com.example.piston.main.groups.group.info.GroupInfoActivity;
import com.example.piston.main.posts.createPost.CreatePostActivity;
import com.example.piston.utilities.MyViewModelFactory;

public class GroupActivity extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        GroupViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory(id))
                .get(GroupViewModel.class);
        ActivityGroupBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_group);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.groupTopAppBar.setNavigationOnClickListener((view) -> onBackPressed());

        binding.recyclerviewGroups.setAdapter(new GroupAdapter(this));
        viewModel.getTitle().observe(this, binding.groupTopAppBar::setTitle);
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("collection", "groups");
        intent.putExtra("document", id);
        startActivity(intent);
    }

    public void goToInfo(View view) {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtra("document", id);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getParentActivityIntent();
        intent.putExtra("tab", 1);
        startActivity(intent);
    }
}
