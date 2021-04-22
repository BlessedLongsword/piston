package com.example.piston.views.global;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.viewmodels.ViewPostsCategoryViewModel;
import com.example.piston.views.posts.CreatePostActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class ViewPostsCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts_category);

        Intent intent = getIntent();
        String title = intent.getStringExtra("id");

        MaterialToolbar toolbar = findViewById(R.id.view_posts_topAppBar);
        toolbar.setTitle(title);

        ViewPostsCategoryViewModel viewModel = new ViewModelProvider(this).get(ViewPostsCategoryViewModel.class);
        //Fer un factory per passar paràmetres al viewModel...
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }
}
