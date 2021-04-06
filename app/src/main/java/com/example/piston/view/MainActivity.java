package com.example.piston.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.piston.R;
import com.example.piston.view.sections.SectionFragment;
import com.example.piston.view.sections.SectionsPagerAdapter;
import com.example.piston.view.user.ViewProfileActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addButton = findViewById(R.id.add_button);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, addButton);
        viewPager = findViewById(R.id.view_pager2);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(
                getResources().getStringArray(R.array.tab_titles)[position])).attach();
    }

    public void add(View view) {
        ((SectionFragment)getSupportFragmentManager().findFragmentByTag(
                "f" + viewPager.getCurrentItem())).add();
    }

    public void openViewProfile(MenuItem item) {
        Intent intent = new Intent(this, ViewProfileActivity.class);
        startActivity(intent);
    }

    public void openNotifications(MenuItem item) {
        Intent intent = new Intent(this, ViewProfileActivity.class);
        startActivity(intent);
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, ViewProfileActivity.class);
        startActivity(intent);
    }
}
