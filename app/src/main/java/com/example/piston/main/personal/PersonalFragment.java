package com.example.piston.main.personal;

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
import com.example.piston.main.personal.createFolder.CreateFolderActivity;
import com.example.piston.databinding.FragmentPersonalBinding;
import com.example.piston.utilities.ScopeFragment;

public class PersonalFragment extends ScopeFragment {

    private PersonalViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentPersonalBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_personal, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(
                PersonalViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.recyclerviewPersonal.setAdapter(new PersonalAdapter(requireActivity()));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setImageResource(R.drawable.baseline_create_new_folder_black_24);
        actionButton.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.personal_secondary)));
    }

    public void add() {
        Intent intent = new Intent(requireActivity(), CreateFolderActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void removeListener() {
        viewModel.removeListener();
    }

}
