package com.example.piston.main.global;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.FragmentGlobalBinding;
import com.example.piston.main.global.createCategory.CreateCategoryActivity;
import com.example.piston.utilities.ScopeFragment;

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
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionButton.setVisibility(buttonVisibility);
        actionButton.setImageResource(R.drawable.outline_collections_black_24);
        actionButton.setBackgroundTintList(ColorStateList.valueOf(getResources()
                .getColor(R.color.global_secondary)));
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
