package com.example.piston.main.global;

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
import com.example.piston.databinding.FragmentGlobalBinding;
import com.example.piston.main.global.createCategory.CreateCategoryActivity;
import com.example.piston.main.sections.SectionFragment;

public class GlobalFragment extends SectionFragment {

    private GlobalViewModel viewModel;

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
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setImageResource(R.drawable.outline_collections_black_24);
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
