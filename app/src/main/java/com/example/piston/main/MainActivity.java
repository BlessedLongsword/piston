package com.example.piston.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.piston.R;
import com.example.piston.main.notifications.NotificationsActivity;
import com.example.piston.main.sections.SectionFragment;
import com.example.piston.main.sections.SectionsPagerAdapter;
import com.example.piston.main.settings.SettingsActivity;
import com.example.piston.authentication.login.LoginActivity;
import com.example.piston.main.profile.ProfileActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.isSignedIn.observe(this, isSignedIn -> {
            if (!isSignedIn)
                goToLogin();
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        viewPager = findViewById(R.id.view_pager2);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(
                getResources().getStringArray(R.array.tab_titles)[position])).attach();

        int tab = getIntent().getIntExtra("tab", 0);
        if (tab != 0)
            tabLayout.selectTab(tabLayout.getTabAt(tab));
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void add(View view) {
        ((SectionFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(
                "f" + viewPager.getCurrentItem()))).add();
    }

    public void openViewProfile(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void openNotifications(MenuItem item) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void logout(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < 3; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag("f" + i);
            if (fragment != null) {
                ((SectionFragment) fragment).removeListener();
            }
        }
        viewModel.logout();
    }
}
