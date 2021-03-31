package com.example.piston.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.piston.R;
import com.example.piston.view.others.SectionsPagerAdapter;
import com.example.piston.viewmodel.PistonViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends PistonActivity {


    MaterialToolbar topAppBar;
    PistonViewModel pistonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        topAppBar = findViewById(R.id.top_app_bar);
        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.navigation_profile) {
                Intent intent = new Intent(this, ViewProfileActivity.class);
                startActivity(intent);
            }
            return false;
        });
    }

    public void add(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }
}