package com.example.piston.view.main.global;

import android.os.Bundle;
import android.view.View;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;

public class CreateCategoryActivity extends PistonActivity {

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_category);
    }

    public void createCategory(View view) {
        onBackPressed();
    }
}
