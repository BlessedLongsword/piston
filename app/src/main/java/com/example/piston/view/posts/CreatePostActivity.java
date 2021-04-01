package com.example.piston.view.posts;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;

public class CreatePostActivity extends PistonActivity {

    TextView title, content;
    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        title = findViewById(R.id.card_title);
        content = findViewById(R.id.card_content);
    }

   /* public void createPost(View view) {
        pistonViewModel.createPost(title.getText().toString(), content.getText().toString(), picturePath);
        onBackPressed();
    }*/

}
