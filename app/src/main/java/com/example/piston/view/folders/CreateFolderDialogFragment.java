package com.example.piston.view.folders;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.viewmodel.PersonalFragmentViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class CreateFolderDialogFragment extends DialogFragment implements View.OnClickListener {

    private PersonalFragmentViewModel personalFragmentViewModel;
    private TextInputLayout title, desc;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_folder, container, false);
        title = view.findViewById(R.id.create_folder_name);
        desc = view.findViewById(R.id.create_folder_description);
        desc.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        Button createFolderBtn = view.findViewById(R.id.create_folder_btn);
        createFolderBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        personalFragmentViewModel = new ViewModelProvider(requireActivity()).get(PersonalFragmentViewModel.class);
    }

    @Override
    public void onClick(View view) {
        personalFragmentViewModel.createFolder(title.getEditText().getText().toString(),
                desc.getEditText().getText().toString());
        dismiss();
    }
}
