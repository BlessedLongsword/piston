package com.example.piston.view.main.group;

import android.content.Intent;
import android.view.View;

import com.example.piston.R;
import com.example.piston.view.main.SectionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupFragment extends SectionFragment {

    public GroupFragment(FloatingActionButton actionButton) {
        super(R.layout.fragment_group, actionButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setImageResource(R.drawable.baseline_group_add_black_24);
    }

    public void add() {
        Intent intent = new Intent(requireActivity(), CreateGroupActivity.class);
        startActivity(intent);
    }
}
