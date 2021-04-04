package com.example.piston.view.main;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class SectionFragment extends Fragment {

    public abstract void select(FloatingActionButton floatingActionButton);
    public abstract void add();

}
