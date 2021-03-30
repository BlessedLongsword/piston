package com.example.piston.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.piston.R;
import com.example.piston.view.others.SectionsPagerAdapter;
import com.example.piston.viewmodel.PistonViewModel;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

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

        initView();
    }

    private void initView() {
        pistonViewModel = new ViewModelProvider(this).get(PistonViewModel.class);
    }

    public void add(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }
}