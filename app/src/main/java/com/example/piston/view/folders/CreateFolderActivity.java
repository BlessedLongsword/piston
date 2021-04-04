package com.example.piston.view.folders;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;
import com.google.android.material.textfield.TextInputLayout;

public class CreateFolderActivity extends PistonActivity {

    TextInputLayout name;
    TextInputLayout desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);
        name = findViewById(R.id.create_folder_name);
        desc = findViewById(R.id.create_folder_description);
        desc.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    public void createFolder(View view) {
        pistonViewModel.setFolderChooser(name.getEditText().getText().toString(), desc.getEditText().getText().toString());
        onBackPressed();
    }
}
