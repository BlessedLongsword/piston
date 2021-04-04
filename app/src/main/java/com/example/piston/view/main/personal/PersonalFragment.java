package com.example.piston.view.main.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.piston.view.main.SectionFragment;
import com.example.piston.viewmodel.PersonalFragmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends SectionFragment {

    ArrayList<String> recyclerList = new ArrayList<>();
    private PersonalFragmentViewModel personalFragmentViewModel;

    public PersonalFragment() {
        Log.d("nowaybro", "created Personal Fragment");
    }

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

        personalFragmentViewModel = new ViewModelProvider(requireActivity())
                .get(PersonalFragmentViewModel.class);
        personalFragmentViewModel.getFolders().observe(requireActivity(),
                folder -> recyclerList = folder);
    }

    @Override
    public void select(FloatingActionButton floatingActionButton) {
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setImageResource(R.drawable.baseline_create_new_folder_black_24);
    }

    @Override
    public void add() {
        Intent intent = new Intent(requireActivity(), CreateFolderActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String desc = data.getStringExtra("desc");
            personalFragmentViewModel.createFolder(title, desc);
        }
    }
}
