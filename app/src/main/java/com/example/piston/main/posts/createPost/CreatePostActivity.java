package com.example.piston.main.posts.createPost;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreatePostBinding;
import com.example.piston.utilities.PickImageActivity;
import com.example.piston.utilities.CheckNetwork;
import com.example.piston.utilities.Values;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CreatePostActivity extends PickImageActivity {

    private ActivityCreatePostBinding binding;
    private CreatePostViewModel createPostViewModel;
    private String scope, sectionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        scope = getIntent().getStringExtra(Values.SCOPE);
        switch (scope) {
            case Values.PERSONAL:
                setTheme(R.style.Theme_Piston_Personal);
                break;
            case Values.GROUPS:
                setTheme(R.style.Theme_Piston_Groups);
                break;
            case Values.GLOBAL:
                setTheme(R.style.Theme_Piston_Global);
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        createPostViewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post);

        sectionID = getIntent().getStringExtra(Values.SECTION_ID);

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
        createPostViewModel.getErrorMessage().observe(this, message -> {
            if (message != null)
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        });
    }

    public void createPost(MenuItem item) {
        boolean connected = CheckNetwork.isConnected(getApplicationContext());
        createPostViewModel.createPost(scope, sectionID, imageUri, connected);
    }

    @Override
    protected void setUri(Uri imageUri) {
        binding.postPicture.setImageURI(imageUri);
    }

}
