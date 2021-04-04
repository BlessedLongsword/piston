package com.example.piston.view.main.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.piston.R;
import com.example.piston.view.main.SectionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupFragment extends SectionFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void select(FloatingActionButton floatingActionButton) {
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setImageResource(R.drawable.baseline_group_add_black_24);
    }

    @Override
    public void add() {
        Intent intent = new Intent(requireActivity(), CreateGroupActivity.class);
        startActivity(intent);
    }
}
