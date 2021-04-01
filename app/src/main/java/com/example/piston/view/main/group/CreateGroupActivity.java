package com.example.piston.view.main.group;

import android.os.Bundle;
import android.view.View;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;

public class CreateGroupActivity extends PistonActivity {

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_group);
    }

    public void createGroup(View view) {
        onBackPressed();
    }
}
