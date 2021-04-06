package com.example.piston.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.piston.R;
import com.example.piston.view.others.NotificationsActivity;
import com.example.piston.view.others.SettingsActivity;
import com.example.piston.view.user.ViewProfileActivity;

import com.google.android.material.appbar.MaterialToolbar;
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


        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        topAppBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.navigation_profile) {
                Intent intent = new Intent(this, ViewProfileActivity.class);
                startActivity(intent);
                return true;
            }
            if (menuItem.getItemId() == R.id.navigation_notifications) {
                Intent intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
                return true;
            }
            if (menuItem.getItemId() == R.id.navigation_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

    }

    public void add(View view) {
        ((SectionFragment)getSupportFragmentManager().findFragmentByTag(
                "f" + viewPager.getCurrentItem())).add();
    }
}
