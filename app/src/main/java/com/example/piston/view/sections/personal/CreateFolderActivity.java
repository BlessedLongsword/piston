package com.example.piston.view.sections.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.viewmodel.PersonalFragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class CreateFolderActivity extends AppCompatActivity {

    private PersonalFragmentViewModel personalFragmentViewModel;
    private TextInputLayout title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);
        personalFragmentViewModel = new ViewModelProvider(this).get(PersonalFragmentViewModel.class);
        title = findViewById(R.id.create_folder_name);
        desc = findViewById(R.id.create_folder_description);
        //desc.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    public void createFolder(MenuItem item) {
        personalFragmentViewModel.createFolder(title.getEditText().getText().toString(),
                desc.getEditText().getText().toString());
        Intent output = new Intent();
        output.putExtra("title", title.getEditText().getText().toString());
        output.putExtra("desc", desc.getEditText().getText().toString());
        setResult(RESULT_OK, output);
        finish();
    }
}
