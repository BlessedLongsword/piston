package com.example.piston.view.posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.google.android.material.appbar.MaterialToolbar;

public class ViewPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        MaterialToolbar toolbar = findViewById(R.id.view_posts_topAppBar);
        toolbar.setTitle(title);
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }

}
