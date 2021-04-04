package com.example.piston.view.main.personal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.view.folders.FolderContainerAdapter;
import com.example.piston.viewmodel.PersonalFragmentViewModel;

import java.util.ArrayList;

public class PersonalFragment extends Fragment {

    ArrayList<String> recyclerList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(new FolderContainerAdapter(requireActivity(), recyclerList));

        PersonalFragmentViewModel personalFragmentViewModel =
                new ViewModelProvider(requireActivity()).get(PersonalFragmentViewModel.class);
        personalFragmentViewModel.getFolders().observe(requireActivity(),
                folder -> recyclerList = folder);
    }
}
