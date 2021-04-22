package com.example.piston.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.piston.main.global.GlobalFragment;
import com.example.piston.main.groupal.GroupFragment;
import com.example.piston.main.personal.PersonalFragment;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new PersonalFragment();
        if (position == 1)
            return new GroupFragment();
        else
            return new GlobalFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}