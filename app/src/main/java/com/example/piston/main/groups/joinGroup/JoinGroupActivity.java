package com.example.piston.main.groups.joinGroup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityJoinGroupBinding;
import com.example.piston.main.groups.createGroup.CreateGroupActivity;

public class JoinGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        JoinGroupViewModel joinGroupViewModel = new ViewModelProvider(this).get(JoinGroupViewModel.class);
        ActivityJoinGroupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_join_group);
        binding.setViewModel(joinGroupViewModel);
        binding.setLifecycleOwner(this);

        binding.joinGroupTopAppBar.setNavigationOnClickListener(v -> finish());

        binding.inputGroupCode.setEndIconOnClickListener(v -> joinGroupViewModel.joinGroup());

        joinGroupViewModel.getJoinedGroup().observe(this, aBoolean -> {
            if (aBoolean) {
                Toast.makeText(this, R.string.join_successful, Toast.LENGTH_LONG).show();
                finish();
            }
        });

        joinGroupViewModel.getCreateGroup().observe(this, aBoolean -> {
            if (aBoolean)
                goToCreateGroup();
        });

    }

    void goToCreateGroup() {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
        finish();
    }

}
