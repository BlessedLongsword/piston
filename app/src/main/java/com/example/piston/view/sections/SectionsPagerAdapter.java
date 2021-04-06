package com.example.piston.view.sections;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.piston.view.sections.global.GlobalFragment;
import com.example.piston.view.sections.group.GroupFragment;
import com.example.piston.view.sections.personal.PersonalFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    FloatingActionButton actionButton;

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity, FloatingActionButton actionButton) {
        super(fragmentActivity);
        this.actionButton = actionButton;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("nowaybro", "Created: " + position);
        if (position == 0)
            return new PersonalFragment(actionButton);
        if (position == 1)
            return new GroupFragment(actionButton);
        else
            return new GlobalFragment(actionButton);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}