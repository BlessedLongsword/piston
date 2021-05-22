package com.example.piston.main.global;

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
import com.example.piston.databinding.FragmentGlobalBinding;
import com.example.piston.main.global.createCategory.CreateCategoryActivity;
import com.example.piston.utilities.ScopeFragment;
import com.example.piston.utilities.Values;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Objects;

public class GlobalFragment extends ScopeFragment {

    private GlobalViewModel viewModel;
    int buttonVisibility;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentGlobalBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_global, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(
                GlobalViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.recyclerviewGlobal.setAdapter(new GlobalAdapter(requireActivity()));
        viewModel.getIsAdmin().observe(Objects.requireNonNull(binding.getLifecycleOwner()),
                aBoolean -> buttonVisibility = aBoolean ? View.VISIBLE : View.GONE);
        viewModel.getFilter().observe(binding.getLifecycleOwner(), s -> {
            switch (s) {
                case (Values.FILTER_MOST_SUBSCRIBERS):
                    binding.filterFieldText.setText(R.string.filter_most_subscribers);
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
        actionButton.setVisibility(buttonVisibility);
        actionButton.setImageResource(R.drawable.outline_collections_black_24);
        actionButton.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.global_secondary)));
    }

    private View.OnClickListener chooseFilter() {
        return v -> {
            new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                    .setTitle(getString(R.string.filter_by))
                    .setItems(R.array.global_filters, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                updateFilter(Values.FILTER_DEFAULT, false);
                                break;
                            case 1:
                                updateFilter(Values.FILTER_MOST_SUBSCRIBERS, true);
                                break;
                            case 2:
                                updateFilter(Values.FILTER_ALPHABETICALLY, false);
                                break;
                        }
                    }).show();
        };
    }

    private void updateFilter(String filter, boolean descending) {
        viewModel.updateFilter(filter, descending);
    }

    public void add() {
        Intent intent = new Intent(requireActivity(), CreateCategoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void removeListener() {
        viewModel.removeListener();
    }
}
