package com.example.piston.views.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.adapters.CategoryAdapter;
import com.example.piston.adapters.FolderAdapter;
import com.example.piston.databinding.FragmentGlobalBinding;
import com.example.piston.databinding.FragmentPersonalBinding;
import com.example.piston.viewmodels.GlobalFragmentViewModel;
import com.example.piston.views.main.SectionFragment;
import com.example.piston.viewmodels.PersonalFragmentViewModel;

import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends SectionFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentPersonalBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_personal, container, false);
        PersonalFragmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(
                PersonalFragmentViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.recyclerviewPersonal.setAdapter(new FolderAdapter(requireActivity()));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setImageResource(R.drawable.baseline_create_new_folder_black_24);
    }

    public void add() {
        Intent intent = new Intent(requireActivity(), CreateFolderActivity.class);
        startActivityForResult(intent, 0);
    }

}
