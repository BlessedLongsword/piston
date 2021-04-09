package com.example.piston.view.sections.global;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.viewmodel.GlobalFragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class CreateCategoryActivity extends AppCompatActivity {

    ClipboardManager clipboard;
    ClipData clip;
    TextInputLayout link, desc, title;
    private GlobalFragmentViewModel globalFragmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_category);

        globalFragmentViewModel = new ViewModelProvider(this).get(GlobalFragmentViewModel.class);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        link = findViewById(R.id.category_link);

        title = findViewById(R.id.input_category_name);

        desc = findViewById(R.id.input_category_desc);
        desc.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        link.setEndIconOnClickListener(v -> {
            clip = ClipData.newPlainText("CategoryCode", link.getEditText().getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.link_copied, Toast.LENGTH_LONG).show();
        });

    }

    public void createCategory(MenuItem item) {
        globalFragmentViewModel.createCategory(title.getEditText().getText().toString(), desc.getEditText().getText().toString());
        Intent output = new Intent();
        output.putExtra("title", title.getEditText().getText().toString());
        output.putExtra("desc", desc.getEditText().getText().toString());
        output.putExtra("link", link.getEditText().getText().toString());
        setResult(RESULT_OK, output);
        finish();
    }
}
