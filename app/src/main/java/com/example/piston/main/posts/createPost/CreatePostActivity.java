package com.example.piston.main.posts.createPost;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreatePostBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreatePostActivity extends AppCompatActivity {

    CreatePostViewModel createPostViewModel;
    String collection, document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        createPostViewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
        ActivityCreatePostBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post);

        collection = getIntent().getStringExtra("collection");
        document = getIntent().getStringExtra("document");

        binding.setViewModel(createPostViewModel);
        binding.setLifecycleOwner(this);
        binding.createPostTopAppBar.setNavigationOnClickListener(v -> finish());

        createPostViewModel.generatePostID();

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
        createPostViewModel.createPost(collection, document);
    }


    //OPEN GALLERY TO SELECT AVATAR
    int PICK_PHOTO_FOR_AVATAR = 0;
    public void imagePick(View v) {
        ImagePicker.Companion.with(this)
                .crop(1.95f, 1f)
                .compress(1024)
                .start();
        /*Log.d("nowaybro", "MMMMMM");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                ImageView im = findViewById(R.id.post_picture);
                Bitmap b = BitmapFactory.decodeStream(inputStream);
                createPostViewModel.uploadImage(b);
                im.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
