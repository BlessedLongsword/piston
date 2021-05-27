package com.example.piston.main.global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.ScopeFragment;
import com.example.piston.utilities.Values;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class GlobalFragment extends ScopeFragment {

    private GlobalViewModel viewModel;
    private SharedPreferences prefs;
    private boolean nsfwVisibility;
    private int buttonVisibility;
    private FragmentGlobalBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        prefs = requireActivity().getSharedPreferences(Values.SHARED_PREFS, Context.MODE_PRIVATE);
        nsfwVisibility = prefs.getBoolean(Values.NSFW_ENABLED, false);


        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_global, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new MyViewModelFactory(nsfwVisibility))
                .get(GlobalViewModel.class);
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

        nsfwVisibility = prefs.getBoolean(Values.NSFW_ENABLED, false);
        viewModel.showNsfw(nsfwVisibility);
    }

    private View.OnClickListener chooseFilter() {
        return v -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setTitle(getString(R.string.filter_by))
                .setItems(R.array.global_filters, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            updateFilter(Values.FILTER_DEFAULT, false, nsfwVisibility);
                            break;
                        case 1:
                            updateFilter(Values.FILTER_MOST_SUBSCRIBERS, true, nsfwVisibility);
                            break;
                        case 2:
                            updateFilter(Values.FILTER_ALPHABETICALLY, false, nsfwVisibility);
                            break;
                    }
                }).show();
    }

    private void updateFilter(String filter, boolean descending, boolean nsfw) {
        viewModel.updateFilter(filter, descending, nsfw);
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
