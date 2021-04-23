package com.example.piston.main.groups;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.FragmentGroupBinding;
import com.example.piston.main.SectionFragment;
import com.example.piston.main.groups.joinGroup.JoinGroupActivity;

public class GroupsFragment extends SectionFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentGroupBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_group, container, false);
        binding.recyclerviewGroup.setAdapter(new GroupAdapter(requireActivity()));
        GroupsViewModel viewModel = new ViewModelProvider(requireActivity())
                .get(GroupsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setImageResource(R.drawable.baseline_group_add_black_24);
    }

    public void add() {
        Intent intent = new Intent(requireActivity(), JoinGroupActivity.class);
        startActivity(intent);
    }

}
