package com.example.piston.view.main;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class SectionFragment extends Fragment {

    protected FloatingActionButton actionButton;

    public SectionFragment(@LayoutRes int contentLayoutId, FloatingActionButton actionButton) {
        super(contentLayoutId);
        this.actionButton = actionButton;
    }

    protected abstract void add();
}
