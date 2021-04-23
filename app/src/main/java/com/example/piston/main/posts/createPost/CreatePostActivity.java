package com.example.piston.main.posts.createPost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.example.piston.main.posts.ViewPostsViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreatePostActivity extends AppCompatActivity {
/*

    CreatePostViewModel createPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        createPostViewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
        ActivityCreatePostBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post);
        binding.setViewModel(createPostViewModel);
        binding.setLifecycleOwner(this);
        binding.createPostTopAppBar.setNavigationOnClickListener(v -> finish());
        createPostViewModel.getCreateError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_category_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> {

                        })
                        .show();
            }
        });
        createPostViewModel.getFinishCreatePost().observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });
    }

    public void createPost(MenuItem item) {
        createPostViewModel.createPost();
    }
*/
    TextInputLayout title, content;

    private ViewPostsViewModel viewPostsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        viewPostsViewModel = new ViewModelProvider(this).get(ViewPostsViewModel.class);

        title = findViewById(R.id.choose_title);
        title.setSuffixText(Integer.toString(getResources().getInteger(R.integer.title_max_length)));
        title.getEditText().addTextChangedListener(new CounterWatcher(R.integer.title_max_length, title));

        content = findViewById(R.id.post_content);
        content.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    public void createPost(MenuItem item) {
        String owner = "jojo";
        viewPostsViewModel.createPost(title.getEditText().getText().toString()
                , owner, content.getEditText().getText().toString());

        Intent output = new Intent();
        output.putExtra("title", title.getEditText().getText().toString());
        output.putExtra("content", content.getEditText().getText().toString());
        output.putExtra("owner", owner);
        setResult(RESULT_OK, output);
        finish();
    }

    /* OPEN GALLERY TO SELECT AVATAR
    int PICK_PHOTO_FOR_AVATAR = 0;
    public void tata(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
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
                im.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }*/

}
