package com.example.piston.view.folders;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;

public class CreateFolderActivity extends PistonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);
    }

    public void createFolder(View view) {
        EditText title = view.findViewById(R.id.input_folder_name);
        EditText desc = view.findViewById(R.id.input_folder_desc);
        pistonViewModel.setFolderChooser(title.getText().toString(), desc.getText().toString());
        onBackPressed();
    }
}
