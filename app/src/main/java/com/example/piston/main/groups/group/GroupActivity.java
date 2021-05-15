package com.example.piston.main.groups.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityGroupBinding;
import com.example.piston.main.MainActivity;
import com.example.piston.main.groups.group.info.GroupInfoActivity;
import com.example.piston.main.notifications.NotificationsActivity;
import com.example.piston.main.posts.createPost.CreatePostActivity;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.Values;

import static com.example.piston.utilities.Values.DELETE_CODE;

public class GroupActivity extends AppCompatActivity {

    private String groupID;
    private boolean orphan;
    private boolean postDidNotExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        groupID = intent.getStringExtra(Values.SECTION_ID);
        orphan = intent.getBooleanExtra("orphan", false);

        postDidNotExist = intent.getBooleanExtra("postDidNotExist", false);

        if (postDidNotExist) {
            orphan = false;
            finish();
        }

        GroupViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory(groupID))
                .get(GroupViewModel.class);
        ActivityGroupBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_group);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.groupTopAppBar.setNavigationOnClickListener((view) -> finish());

        binding.recyclerviewGroups.setAdapter(new GroupAdapter(this));
        viewModel.getTitle().observe(this, binding.groupTopAppBar::setTitle);
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra(Values.SCOPE, Values.GROUPS);
        intent.putExtra(Values.SECTION_ID, groupID);
        startActivity(intent);
    }

    @Override
    public void finish() {
        if (orphan) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("tab", 1);
            startActivity(intent);
        }
        if (postDidNotExist) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
        }
        super.finish();
    }

    public void goToInfo(View view) {
        goToInfo();
    }

    public void goToInfo(MenuItem item) {
        goToInfo();
    }

    public void goToInfo() {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtra(Values.SECTION_ID, groupID);
        startActivityForResult(intent, DELETE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == DELETE_CODE)
            finish();
    }
}
