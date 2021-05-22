package com.example.piston.main.groups;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.FragmentGroupBinding;
import com.example.piston.utilities.ScopeFragment;
import com.example.piston.main.groups.joinGroup.JoinGroupActivity;
import com.example.piston.utilities.Values;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class GroupsFragment extends ScopeFragment {

    private GroupsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentGroupBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_group, container, false);
        binding.recyclerviewGroup.setAdapter(new GroupsAdapter(requireActivity()));
        viewModel = new ViewModelProvider(requireActivity())
                .get(GroupsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.getFilter().observe(Objects.requireNonNull(binding.getLifecycleOwner()), s -> {
            switch (s) {
                case (Values.FILTER_MOST_MEMBERS):
                    binding.filterFieldText.setText(R.string.filter_most_members);
                    break;
                case (Values.FILTER_ALPHABETICALLY):
                    binding.filterFieldText.setText(R.string.filter_alphabetically);
                    break;
                default:
                    binding.filterFieldText.setText(R.string.filter_default);
                    break;
            }
        });
        binding.filterField.setOnClickListener(chooseFilter());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setImageResource(R.drawable.baseline_group_add_black_24);
        actionButton.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.groups_secondary)));
    }

    private View.OnClickListener chooseFilter() {
        return v -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setTitle(getString(R.string.filter_by))
                .setItems(R.array.groups_filters, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            updateFilter(Values.FILTER_DEFAULT, false);
                            break;
                        case 1:
                            updateFilter(Values.FILTER_MOST_MEMBERS, true);
                            break;
                        case 2:
                            updateFilter(Values.FILTER_ALPHABETICALLY, false);
                            break;
                    }
                }).show();
    }

    private void updateFilter(String filter, boolean descending) {
        viewModel.updateFilter(filter, descending);
    }

    public void add() {
        Intent intent = new Intent(requireActivity(), JoinGroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void removeListener() {
        viewModel.removeListener();
    }

}
