package com.example.piston.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.piston.R;
import com.example.piston.main.groups.group.GroupActivity;
import com.example.piston.main.notifications.NotificationsActivity;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.utilities.ScopeFragment;
import com.example.piston.utilities.ScopePagerAdapter;
import com.example.piston.main.settings.SettingsActivity;
import com.example.piston.authentication.login.LoginActivity;
import com.example.piston.main.profile.ProfileActivity;

import com.example.piston.utilities.Values;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ViewPager2 viewPager;
    private String scope;
    private String sectionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.isSignedIn.observe(this, isSignedIn -> {
            if (!isSignedIn)
                goToLogin();
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scope = getIntent().getStringExtra(Values.SCOPE);
        sectionID = getIntent().getStringExtra(Values.SECTION_ID);

        ScopePagerAdapter scopePagerAdapter = new ScopePagerAdapter(this);
        viewPager = findViewById(R.id.view_pager2);
        viewPager.setAdapter(scopePagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(
                getResources().getStringArray(R.array.tab_titles)[position])).attach();

        if (getIntent().getBooleanExtra(Values.FROM_SHARE, false)) {
            if (scope.equals(Values.GROUPS) || scope.equals(Values.JOIN))
                viewModel.checkFromShareBelongsToGroup(sectionID);
            else
                goToSharedPost();
        }

        viewModel.getFromShareBelongsToGroup().observe(this, aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) {
                    if (scope.equals(Values.GROUPS))
                        goToSharedPost();
                    else {
                        goToSharedGroup(false);
                        Toast.makeText(this, R.string.join_group_already, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    if (scope.equals(Values.GROUPS))
                        Toast.makeText(this, R.string.not_belongs_to_group, Toast.LENGTH_LONG).show();
                    else {
                        goToSharedGroup(true);
                    }
                }
            }
        });

        int tab = getIntent().getIntExtra("tab", 0);
        if (tab != 0)
            tabLayout.selectTab(tabLayout.getTabAt(tab));
    }

    private void goToSharedPost() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(Values.SCOPE, scope);
        intent.putExtra(Values.SECTION_ID, sectionID);
        intent.putExtra(Values.POST_ID, getIntent().getStringExtra(Values.POST_ID));
        intent.putExtra(Values.ORPHAN, true);
        startActivity(intent);
    }

    private void goToSharedGroup(boolean alreadyJoined) {
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra(Values.SECTION_ID, sectionID);
        intent.putExtra(Values.FROM_SHARE_GROUP, alreadyJoined);
        intent.putExtra(Values.ORPHAN, true);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void add(View view) {
        ((ScopeFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(
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
                ((ScopeFragment) fragment).removeListener();
            }
        }
        viewModel.logout();
    }
}
