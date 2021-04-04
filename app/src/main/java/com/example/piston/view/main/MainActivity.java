package com.example.piston.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.piston.R;
import com.example.piston.view.folders.CreateFolderDialogFragment;
import com.example.piston.view.main.global.CreateCategoryActivity;
import com.example.piston.view.main.group.CreateGroupActivity;
import com.example.piston.view.others.NotificationsActivity;
import com.example.piston.view.others.SettingsActivity;
import com.example.piston.view.user.ViewProfileActivity;
import com.example.piston.viewmodel.MainActivityViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

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
            public void onTabSelected(TabLayout.Tab tab) {
                mainActivityViewModel.setActiveTab(tab.getPosition());
                if (tab.getPosition() == 0)
                    addButton.setImageResource(R.drawable.baseline_create_new_folder_black_24);
                if (tab.getPosition() == 1)
                    addButton.setImageResource(R.drawable.baseline_group_add_black_24);
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
        if (mainActivityViewModel.getActiveTab() == 0) {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment personalFragment = new CreateFolderDialogFragment();
            personalFragment.show(fm, "Where am I");
        }
        else if (mainActivityViewModel.getActiveTab() == 1) {
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, CreateCategoryActivity.class);
            startActivity(intent);
        }
    }

}
