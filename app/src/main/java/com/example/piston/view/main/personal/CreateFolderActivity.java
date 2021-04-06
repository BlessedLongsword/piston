package com.example.piston.view.main.personal;

import android.content.Intent;
import android.os.Bundle;
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

    public void createFolder(View view) {
        personalFragmentViewModel.createFolder(title.getEditText().getText().toString(),
                desc.getEditText().getText().toString());
        Intent output = new Intent();
        output.putExtra("title", title.getEditText().getText().toString());
        output.putExtra("desc", desc.getEditText().getText().toString());
        setResult(RESULT_OK, output);
        finish();
    }
}


/*package com.example.piston.view.main.personal;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.viewmodel.PersonalFragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class CreateFolderActivity extends AppCompatActivity {

    private PersonalFragmentViewModel personalFragmentViewModel;
    private TextInputLayout title, desc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_folder, container, false);
        title = view.findViewById(R.id.create_folder_name);
        desc = view.findViewById(R.id.create_folder_description);
        desc.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        Button createFolderBtn = view.findViewById(R.id.create_folder_btn);
        createFolderBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        personalFragmentViewModel = new ViewModelProvider(requireActivity()).get(PersonalFragmentViewModel.class);
    }

    @Override
    public void onClick(View view) {
        personalFragmentViewModel.createFolder(title.getEditText().getText().toString(),
                desc.getEditText().getText().toString());
        dismiss();
    }
}
*/