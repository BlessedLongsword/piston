package com.example.piston.utilities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.piston.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public abstract class ScopeFragment extends Fragment {

    protected FloatingActionButton actionButton;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionButton = Objects.requireNonNull(getActivity()).findViewById(R.id.add_button);
    }

    public abstract void add();

    public abstract void removeListener();
}
