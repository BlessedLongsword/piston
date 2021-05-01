package com.example.piston.main.posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityPostsBinding;
import com.example.piston.main.posts.createPost.CreatePostActivity;
import com.example.piston.utilities.MyViewModelFactory;

public class PostsActivity extends AppCompatActivity {

    private String collection, document;
    private PostsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Intent intent = getIntent();
        document = intent.getStringExtra("collection");

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(document))
                .get(PostsViewModel.class);
        ActivityPostsBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_posts);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.postsTopAppBar.setTitle(document);
        binding.postsTopAppBar.setNavigationOnClickListener((view) -> finish());

        binding.recyclerviewPosts.setAdapter(new PostsAdapter(this));
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("collection", "users");
        intent.putExtra("document", document);
        startActivity(intent);
    }
}
