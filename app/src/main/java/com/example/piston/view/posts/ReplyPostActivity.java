package com.example.piston.view.posts;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.google.android.material.textfield.TextInputLayout;

public class ReplyPostActivity extends AppCompatActivity {

    private TextInputLayout content;
    private Button replyBtn;

    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_group);

        content = findViewById(R.id.post_content);
        replyBtn = findViewById(R.id.reply_btn);
        content.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    public void replyPost(View view) {
        onBackPressed();
    }

}
