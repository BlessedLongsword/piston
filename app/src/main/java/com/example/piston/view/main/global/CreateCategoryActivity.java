package com.example.piston.view.main.global;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.google.android.material.textfield.TextInputLayout;

public class CreateCategoryActivity extends AppCompatActivity {

    ClipboardManager clipboard;
    ClipData clip;
    TextInputLayout link, desc;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_category);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        link = findViewById(R.id.category_link);

        desc = findViewById(R.id.input_category_desc);
        desc.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        link.setEndIconOnClickListener(v -> {
            clip = ClipData.newPlainText("CategoryCode", link.getEditText().getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.link_copied, Toast.LENGTH_LONG).show();
        });

    }

    public void createCategory(View view) {
        onBackPressed();
    }
}
