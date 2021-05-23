package com.example.piston.main.groups.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class GroupActivity extends AppCompatActivity {

    private String groupID;
    private boolean orphan;
    private boolean postDidNotExist;
    private boolean fromShareGroup;
    GroupViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        groupID = intent.getStringExtra(Values.SECTION_ID);
        orphan = intent.getBooleanExtra(Values.ORPHAN, false);

        postDidNotExist = intent.getBooleanExtra(Values.POST_DOES_NOT_EXIST, false);
        fromShareGroup = intent.getBooleanExtra(Values.FROM_SHARE_GROUP, false);

        if (postDidNotExist) {
            orphan = false;
            finish();
        }

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(groupID))
                .get(GroupViewModel.class);
        ActivityGroupBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_group);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.groupTopAppBar.setNavigationOnClickListener((view) -> finish());

        binding.recyclerviewGroups.setAdapter(new GroupAdapter(this));
        viewModel.getTitle().observe(this, binding.groupTopAppBar::setTitle);

        if (fromShareGroup)
            viewModel.fromShareJoinGroup(getIntent().getStringExtra(Values.SECTION_ID));

        viewModel.getFromShareJoinedGroup().observe(this, aBoolean -> {
            if (aBoolean)
                Toast.makeText(this, R.string.join_successful, Toast.LENGTH_LONG).show();
        });
        viewModel.getFilter().observe(this, s -> {
            if (Values.FILTER_ALPHABETICALLY.equals(s)) {
                binding.filterFieldText.setText(R.string.filter_alphabetically);
            } else {
                binding.filterFieldText.setText(R.string.filter_default);
            }
        });
        binding.filterField.setOnClickListener(chooseFilter());
        viewModel.getModMode().observe(this, aBoolean -> {
            if (aBoolean) {
                int priority = Objects.requireNonNull(viewModel.getPriority().getValue());
                if (priority > 1)
                    binding.addButton.setVisibility(View.GONE);
                else
                    binding.addButton.setVisibility(View.VISIBLE);
            } else
                binding.addButton.setVisibility(View.VISIBLE);
        });
        viewModel.getPriority().observe(this, integer -> {
            if (integer > 1) {
                boolean modMode = Objects.requireNonNull(viewModel.getModMode().getValue());
                if (modMode)
                    binding.addButton.setVisibility(View.GONE);
                else
                    binding.addButton.setVisibility(View.VISIBLE);
            } else
                binding.addButton.setVisibility(View.VISIBLE);
        });
    }

    @SuppressWarnings("unused")
    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra(Values.SCOPE, Values.GROUPS);
        intent.putExtra(Values.SECTION_ID, groupID);
        startActivity(intent);
    }

    private View.OnClickListener chooseFilter() {
        return v -> new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.filter_by))
                .setItems(R.array.group_filters, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            updateFilter(Values.FILTER_DEFAULT);
                            break;
                        case 1:
                            updateFilter(Values.FILTER_ALPHABETICALLY);
                            break;
                    }
                }).show();
    }

    private void updateFilter(String filter) {
        viewModel.updateFilter(filter, false);
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

    @SuppressWarnings("unused")
    public void goToInfo(View view) {
        goToInfo();
    }

    @SuppressWarnings("unused")
    public void goToInfo(MenuItem item) {
        goToInfo();
    }

    public void goToInfo() {
        if (viewModel.getFromShareJoinedGroup() != null) {
            if (Objects.requireNonNull(viewModel.getFromShareJoinedGroup().getValue()) ||
                    !fromShareGroup) {
                Intent intent = new Intent(this, GroupInfoActivity.class);
                intent.putExtra(Values.SECTION_ID, groupID);
                startActivityForResult(intent, Values.DELETE_CODE);
            }
        }
    }

    @SuppressWarnings("unused")
    public void shareGroup(MenuItem item) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.piston.com/" + Values.JOIN + "/" +
                groupID);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_post)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Values.DELETE_CODE)
            finish();
    }
}
