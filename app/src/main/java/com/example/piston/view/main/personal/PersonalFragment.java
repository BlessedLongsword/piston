package com.example.piston.view.main.personal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.view.PistonFragment;
import com.example.piston.view.folders.FolderContainerAdapter;

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
        pistonViewModel.getFolderChooser().observe(getViewLifecycleOwner(),
                folderChooser -> recyclerList = folderChooser);
        for (int i = 0; i < 50; i++)
            recyclerList.add(String.valueOf(i));
        return view;
    }



}
