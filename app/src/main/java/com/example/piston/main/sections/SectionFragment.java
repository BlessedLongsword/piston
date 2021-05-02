package com.example.piston.main.sections;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.piston.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class SectionFragment extends Fragment {

    protected FloatingActionButton actionButton;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionButton = getActivity().findViewById(R.id.add_button);
    }

    public abstract void add();

    public abstract void removeListener();
}
