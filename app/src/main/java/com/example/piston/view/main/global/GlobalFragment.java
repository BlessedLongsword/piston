package com.example.piston.view.main.global;

import android.content.Intent;
import android.view.View;

import com.example.piston.R;
import com.example.piston.view.main.SectionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GlobalFragment extends SectionFragment {

    public GlobalFragment(FloatingActionButton actionButton) {
        super(R.layout.fragment_global, actionButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        actionButton.setVisibility(View.INVISIBLE);
    }

    public void add() {
        Intent intent = new Intent(requireActivity(), CreateCategoryActivity.class);
        startActivity(intent);
    }
}
