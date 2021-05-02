package com.example.piston.main.posts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityPostBinding;
import com.example.piston.utilities.MyViewModelFactory;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (savedInstanceState != null) {
        }

        Intent intent = getIntent();
        String collection = intent.getStringExtra("collection");
        String document = intent.getStringExtra("document");
        String postID = intent.getStringExtra("id");

        PostViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory(collection, document, postID))
                .get(PostViewModel.class);
        ActivityPostBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_post);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.postsTopAppBar.setNavigationOnClickListener((view) -> finish());
        binding.recyclerviewPosts.setAdapter(new PostAdapter(this));

        viewModel.getPostTitle().observe(this, binding.postsTopAppBar::setTitle);
    }

}
