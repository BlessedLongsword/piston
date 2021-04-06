package com.example.piston.view.sections.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.view.sections.SectionFragment;
import com.example.piston.viewmodel.PersonalFragmentViewModel;

import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends SectionFragment {

    private PersonalFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(PersonalFragmentViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_personal);
        recyclerView.setAdapter(new FolderAdapter(requireActivity()));
        return view;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String desc = data.getStringExtra("desc");
            viewModel.createFolder(title, desc);
        }
    }
}
