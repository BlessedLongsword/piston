package com.example.piston.views.posts;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.example.piston.viewmodels.GlobalFragmentViewModel;
import com.example.piston.viewmodels.ViewPostsActivityViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class CreatePostActivity extends AppCompatActivity {

    TextInputLayout title, content;

    private ViewPostsActivityViewModel viewPostsActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        viewPostsActivityViewModel = new ViewModelProvider(this).get(ViewPostsActivityViewModel.class);

        title = findViewById(R.id.choose_title);
        title.setSuffixText(Integer.toString(getResources().getInteger(R.integer.title_max_length)));
        title.getEditText().addTextChangedListener(new CounterWatcher(R.integer.title_max_length, title));

        content = findViewById(R.id.post_content);
        content.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    public void createPost(MenuItem item) {
        String owner = "jojo";
        viewPostsActivityViewModel.createPost(title.getEditText().getText().toString()
                , owner, content.getEditText().getText().toString());

        Intent output = new Intent();
        output.putExtra("title", title.getEditText().getText().toString());
        output.putExtra("content", content.getEditText().getText().toString());
        output.putExtra("owner", owner);
        setResult(RESULT_OK, output);
        finish();
    }
}
