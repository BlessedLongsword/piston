package com.example.piston.views.global;

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
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.example.piston.viewmodels.GlobalFragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class CreateCategoryActivity extends AppCompatActivity {

    TextInputLayout desc, title;
    private GlobalFragmentViewModel globalFragmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_category);

        globalFragmentViewModel = new ViewModelProvider(this).get(GlobalFragmentViewModel.class);

        title = findViewById(R.id.input_category_name);
        title.setSuffixText(Integer.toString(getResources().getInteger(R.integer.title_max_length)));
        title.getEditText().addTextChangedListener(new CounterWatcher(R.integer.title_max_length, title));

        desc = findViewById(R.id.input_category_desc);
        desc.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);

    }

    public void createCategory(MenuItem item) {
        //globalFragmentViewModel.createCategory(title.getEditText().getText().toString(), desc.getEditText().getText().toString());
        finish();
    }
}
