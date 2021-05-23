package com.example.piston.main.groups.group.info;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityGroupInfoBinding;
import com.example.piston.utilities.EditPopup;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.Values;

import java.util.Objects;

public class GroupInfoActivity extends AppCompatActivity {

    private GroupInfoViewModel viewModel;
    private ClipboardManager clipboard;
    private ActivityGroupInfoBinding binding;
    private String groupID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        Intent intent = getIntent();
        groupID = intent.getStringExtra(Values.SECTION_ID);

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(groupID))
                .get(GroupInfoViewModel.class);
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_group_info);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        binding.recyclerviewMembers.setAdapter(new MemberAdapter(this));

        registerForContextMenu(binding.recyclerviewMembers);

        binding.groupInfoTopAppBar.setNavigationOnClickListener((view) -> finish());
        viewModel.getPriority().observe(this, priority -> {
            binding.groupInfoTopAppBar.getMenu().getItem(1).setVisible(priority<=1);
            binding.groupInfoTopAppBar.getMenu().getItem(2).setVisible(priority<=1);
            if (priority<=1) {
                binding.groupInfoDescriptionCard.setOnClickListener(v -> editDescription());
                if (priority == 0) {
                    binding.moderatorModeSwitch.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getTitle().observe(this, binding.groupInfoTopAppBar::setTitle);
        viewModel.getImageLink().observe(this, aString -> Glide.with(this)
            .load(aString)
            .into(binding.groupInfoImage));
        viewModel.getModMode().observe(this, aBoolean ->
                binding.moderatorModeSwitch.setChecked(aBoolean));
        binding.moderatorModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.setModMode(isChecked));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        int position;

        try {
            position = ((MemberAdapter) Objects.requireNonNull(binding.recyclerviewMembers
                    .getAdapter())).getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }

        String memberEmail = Objects.requireNonNull(viewModel.getMembers().getValue()).get(position).getEmail();

        if (item.getItemId() == R.id.ctx_menu_members_mod) {
            viewModel.updateMemberPriority(memberEmail, 1);
        } else if (item.getItemId() == R.id.ctx_menu_members_dismiss_mod) {
            viewModel.updateMemberPriority(memberEmail, 2);
        } else {
            viewModel.removeMember(memberEmail);
        }

        return super.onContextItemSelected(item);
    }

    @SuppressWarnings("unused")
    public void deleteGroup(MenuItem item) {
        viewModel.deleteGroup();
        setResult(Values.DELETE_CODE);
        finish();
    }

    public void editDescription() {
        viewModel.reset();
        EditPopup popup = new EditPopup(this, getString(R.string.description),
                binding.groupInfoDescription.getText().toString());
        popup.getSaveButton().setOnClickListener(v -> viewModel.editDescription(popup.getText()));
        viewModel.getFinished().observe(this, finished -> {
            if (finished) {
                popup.dismiss();
                viewModel.update();
            }
        });
    }

    @SuppressWarnings("unused")
    public void copyGroupID(View v) {
        ClipData clip = ClipData.newPlainText("GroupCode",
                Objects.requireNonNull(Objects.requireNonNull(binding.groupId).getText().toString()));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.link_copied, Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("unused")
    public void shareGroup(MenuItem item) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.piston.com/" + Values.JOIN + "/" +
                groupID);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_post)));
    }

    @SuppressWarnings("unused")
    public void editTitle(MenuItem item) {
        viewModel.reset();
        EditPopup popup = new EditPopup(this, getString(R.string.title),
                binding.groupInfoTopAppBar.getTitle().toString());
        popup.getSaveButton().setOnClickListener(v -> viewModel.editTitle(popup.getText()));
        viewModel.getFinished().observe(this, finished -> {
            if (finished) {
                popup.dismiss();
                viewModel.update();
            }
        });
    }
}
