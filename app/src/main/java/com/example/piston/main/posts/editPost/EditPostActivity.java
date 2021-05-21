package com.example.piston.main.posts.editPost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityEditPostBinding;
import com.example.piston.utilities.CheckNetwork;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.PickImageActivity;
import com.example.piston.utilities.Values;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class EditPostActivity extends PickImageActivity {

    private ActivityEditPostBinding binding;
    private EditPostViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String scope = intent.getStringExtra(Values.SCOPE);
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
        setContentView(R.layout.activity_edit_post);

        String sectionID = intent.getStringExtra(Values.SECTION_ID);
        String postID = intent.getStringExtra(Values.POST_ID);

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(scope, sectionID, postID))
                .get(EditPostViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_post);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getImageLink().observe(this, imageLink -> Glide.with(this)
                .load(imageLink)
                .into(binding.postPicture));

        binding.editPostTopAppBar.setNavigationOnClickListener((view) -> finish());

        viewModel.getEditError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_post_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> { })
                        .show();
            }
        });
        viewModel.getFinishEditPost().observe(this, aBoolean -> {
            if (aBoolean) {
                setResult(Values.EDIT_CODE);
                finish();
            }
        });
        viewModel.getErrorMessage().observe(this, aBoolean -> {
            if (aBoolean)
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        });
    }

    @SuppressWarnings("unused")
    public void editPost(MenuItem item) {
        boolean connected = CheckNetwork.isConnected(getApplicationContext());
        viewModel.editPost(imageUri, connected);
    }

    @Override
    protected void setUri(Uri imageUri) {
        binding.postPicture.setImageURI(imageUri);
    }
}