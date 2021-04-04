package com.example.piston.view.main.global;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.piston.R;
import com.example.piston.view.main.SectionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GlobalFragment extends SectionFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_global, container, false);
    }

    @Override
    public void select(FloatingActionButton floatingActionButton) {
        floatingActionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void add() {
        Intent intent = new Intent(requireActivity(), CreateCategoryActivity.class);
        startActivity(intent);
    }
}
