package com.example.piston.view.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.piston.R;
import com.example.piston.view.main.global.GlobalFragment;
import com.example.piston.view.main.group.GroupFragment;
import com.example.piston.view.main.personal.PersonalFragment;

import org.jetbrains.annotations.NotNull;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final SectionFragment[] sections;

    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.tab_personal, R.string.tab_group, R.string.tab_global};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        sections = new SectionFragment[] {null, null, null};
    }

    @NotNull
    @Override
    public SectionFragment getItem(int position) {
        switch (position) {
            case 0:
                if (sections[0] == null)
                    sections[0] = new PersonalFragment();
                return sections[0];
            case 1:
                if (sections[1] == null)
                    sections[1] = new GroupFragment();
                return sections[1];
            default:
                if (sections[2] == null)
                    sections[2] = new GlobalFragment();
                return sections[2];
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return sections.length;
    }
}