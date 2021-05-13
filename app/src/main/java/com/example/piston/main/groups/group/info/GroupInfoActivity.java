package com.example.piston.main.groups.group.info;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityGroupInfoBinding;
import com.example.piston.utilities.MyViewModelFactory;

import java.util.Objects;

public class GroupInfoActivity extends AppCompatActivity {

    private GroupInfoViewModel viewModel;
    private ClipboardManager clipboard;
    private ClipData clip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        Intent intent = getIntent();
        String title = intent.getStringExtra("document");

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(title))
                .get(GroupInfoViewModel.class);
        ActivityGroupInfoBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_group_info);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        binding.recyclerviewMembers.setAdapter(new MemberAdapter(this));

        binding.groupLink.setEndIconOnClickListener(v -> {
            clip = ClipData.newPlainText("GroupCode", Objects.requireNonNull(binding.groupLink.getEditText()).getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.link_copied, Toast.LENGTH_LONG).show();
        });

        binding.groupInfoTopAppBar.setNavigationOnClickListener((view) -> finish());

        viewModel.getTitle().observe(this, binding.groupInfoTopAppBar::setTitle);
        viewModel.getImageLink().observe(this, aString -> Glide.with(this)
            .load(aString)
            .into(binding.groupInfoImage));
    }

    public void deleteGroup(MenuItem item) {
        viewModel.deleteGroup();
        finish();
    }

}
