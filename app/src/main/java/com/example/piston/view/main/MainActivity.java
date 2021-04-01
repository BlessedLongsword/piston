package com.example.piston.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.piston.R;
import com.example.piston.view.main.global.CreateCategoryActivity;
import com.example.piston.view.main.group.CreateGroupActivity;
import com.example.piston.view.others.NotificationsActivity;
import com.example.piston.view.PistonActivity;
import com.example.piston.view.others.SettingsActivity;
import com.example.piston.view.posts.CreatePostActivity;
import com.example.piston.view.user.ViewProfileActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends PistonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

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

        FloatingActionButton addButton = findViewById(R.id.add_button);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { //Canviar icona en cadascun?
                pistonViewModel.setActiveTab(tab.getPosition());
                if (tab.getPosition() != 2)
                    addButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                addButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    public void add(View view) {
        if (pistonViewModel.getActiveTab() == 0) {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivity(intent);
        }
        else if (pistonViewModel.getActiveTab() == 1) {
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, CreateCategoryActivity.class);
            startActivity(intent);
        }
    }
}
