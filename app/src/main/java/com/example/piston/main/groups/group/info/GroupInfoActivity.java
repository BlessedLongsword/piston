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

import static com.example.piston.utilities.Values.DELETE_CODE;

public class GroupInfoActivity extends AppCompatActivity {

    private GroupInfoViewModel viewModel;
    private ClipboardManager clipboard;
    private ClipData clip;
    private ActivityGroupInfoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        Intent intent = getIntent();
        String groupID = intent.getStringExtra(Values.SECTION_ID);

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
            binding.groupInfoTopAppBar.getMenu().getItem(0).setVisible(priority==0);
            binding.groupInfoTopAppBar.getMenu().getItem(1).setVisible(priority<=1);
            if (priority<=1) {
                binding.groupInfoDescriptionCard.setOnClickListener(v -> editDescription());
            }
        });
        viewModel.getTitle().observe(this, binding.groupInfoTopAppBar::setTitle);
        viewModel.getImageLink().observe(this, aString -> Glide.with(this)
            .load(aString)
            .into(binding.groupInfoImage));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        int position = -1;

        try {
            position = ((MemberAdapter) Objects.requireNonNull(binding.recyclerviewMembers
                    .getAdapter())).getPosition();
        } catch (Exception e) {
            Log.w("DBReadTAG", e.getLocalizedMessage(), e);
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

    public void deleteGroup(MenuItem item) {
        viewModel.deleteGroup();
        setResult(DELETE_CODE);
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

    public void copyGroupID(View v) {
        clip = ClipData.newPlainText("GroupCode",
                Objects.requireNonNull(Objects.requireNonNull(binding.groupId).getText().toString()));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.link_copied, Toast.LENGTH_LONG).show();
    }

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
