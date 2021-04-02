package com.example.piston.view.main.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.view.PistonFragment;
import com.example.piston.view.folders.FolderContainerAdapter;
import com.example.piston.view.main.MainActivity;
import com.example.piston.viewmodel.PistonViewModel;

import java.util.ArrayList;

public class PersonalFragment extends PistonFragment {

    ArrayList<String> recyclerList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(new FolderContainerAdapter(view.getContext(), recyclerList));
        pistonViewModel.getFolderChooser().observe(getViewLifecycleOwner(), folderChooser -> {
            recyclerList = folderChooser;
        });
        return view;
    }



}
