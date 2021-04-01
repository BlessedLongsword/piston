package com.example.piston.view.posts;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;

public class EditPostActivity extends PistonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
    }

    public void editPost(View view) {
        onBackPressed();
    }

}
