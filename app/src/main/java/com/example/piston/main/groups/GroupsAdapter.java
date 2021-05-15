package com.example.piston.main.groups;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.data.sections.Group;
import com.example.piston.databinding.ItemGroupBinding;
import com.example.piston.main.groups.group.GroupActivity;
import com.example.piston.utilities.Values;

import java.util.Objects;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsHolder> {

    private final FragmentActivity localActivity;
    private final GroupsViewModel viewModel;

    public static class GroupsHolder extends RecyclerView.ViewHolder {

        private final ItemGroupBinding binding;

        public GroupsHolder(ItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Group item) { binding.setGroup(item); }

        public ItemGroupBinding getBinding() { return binding; }
    }

    public GroupsAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GroupsViewModel.class);
        viewModel.getGroups().observe(activity, item -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public GroupsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemGroupBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_group, parent, false);
        return new GroupsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.GroupsHolder holder, int position) {
        Group group = Objects.requireNonNull(Objects.requireNonNull(viewModel.getGroups().getValue()).get(position));
        holder.bind(group);
        Glide.with(localActivity)
                .load(group.getImageLink())
                .into(holder.binding.groupImage);
        holder.getBinding().groupItemCard.setOnClickListener(openNewActivity(group.getId()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getGroups().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String groupID) {
        return v -> {
            Intent intent = new Intent(localActivity, GroupActivity.class);
            intent.putExtra(Values.SECTION_ID, groupID);
            localActivity.startActivity(intent);
        };
    }

}