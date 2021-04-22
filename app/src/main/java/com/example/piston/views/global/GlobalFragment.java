package com.example.piston.views.global;

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
import com.example.piston.adapters.CategoryAdapter;
import com.example.piston.databinding.FragmentGlobalBinding;
import com.example.piston.views.main.SectionFragment;
import com.example.piston.viewmodels.GlobalFragmentViewModel;

public class GlobalFragment extends SectionFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentGlobalBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_global, container, false);
        GlobalFragmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(
                GlobalFragmentViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.recyclerviewGlobal.setAdapter(new CategoryAdapter(requireActivity()));
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
}
