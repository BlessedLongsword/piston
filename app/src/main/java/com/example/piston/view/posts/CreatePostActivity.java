package com.example.piston.view.posts;

import android.os.Bundle;
import android.text.InputType;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.google.android.material.textfield.TextInputLayout;

public class CreatePostActivity extends AppCompatActivity {

    TextInputLayout title;
    TextInputLayout postContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        postContent = findViewById(R.id.post_content);
        postContent.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

   /* public void createPost(View view) {
        pistonViewModel.createPost(title.getText().toString(), content.getText().toString(), picturePath);
        onBackPressed();
    }*/

}
