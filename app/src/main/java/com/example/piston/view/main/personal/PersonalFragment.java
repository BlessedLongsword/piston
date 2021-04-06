package com.example.piston.view.main.personal;

import android.content.Intent;
import android.view.View;

import com.example.piston.R;
import com.example.piston.view.main.SectionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends SectionFragment {

    public PersonalFragment(FloatingActionButton actionButton) {
        super(R.layout.fragment_personal, actionButton);
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
            //personalFragmentViewModel.createFolder(title, desc);
        }
    }
}
