package com.example.piston.main.posts.createPost;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreatePostBinding;
import com.example.piston.main.sections.PickImageActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CreatePostActivity extends PickImageActivity {

    private ActivityCreatePostBinding binding;
    private CreatePostViewModel createPostViewModel;
    private String collection, document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        createPostViewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post);

        collection = getIntent().getStringExtra("collection");
        document = getIntent().getStringExtra("document");

        binding.setViewModel(createPostViewModel);
        binding.setLifecycleOwner(this);
        binding.createPostTopAppBar.setNavigationOnClickListener(v -> finish());

        createPostViewModel.getCreateError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_post_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> { })
                        .show();
            }
        });
        createPostViewModel.getFinishCreatePost().observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });
    }

    public void createPost(MenuItem item) {
        createPostViewModel.createPost(collection, document, image);
    }

    @Override
    protected void setImage(Bitmap bitmap) {
        binding.postPicture.setImageBitmap(bitmap);
    }

}
