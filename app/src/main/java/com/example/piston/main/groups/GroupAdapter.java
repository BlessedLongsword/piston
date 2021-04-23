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

import com.example.piston.R;
import com.example.piston.data.Group;
import com.example.piston.databinding.ItemGroupBinding;
import com.example.piston.main.global.category.CategoryActivity;
import com.example.piston.main.groups.group.GroupActivity;

import java.util.Objects;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {

    private final FragmentActivity localActivity;
    private final GroupsViewModel viewModel;

    public static class GroupHolder extends RecyclerView.ViewHolder {

        private final ItemGroupBinding binding;

        public GroupHolder(ItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Group item) { binding.setGroup(item); }

        public ItemGroupBinding getBinding() { return binding; }
    }

    public GroupAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GroupsViewModel.class);
        viewModel.getGroups().observe(activity, item -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemGroupBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_group, parent, false);
        return new GroupHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupHolder holder, int position) {
        Group group = Objects.requireNonNull(viewModel.getGroups().getValue().get(position));
        holder.bind(group);
        holder.getBinding().groupItemCard.setOnClickListener(openNewActivity(group.getId()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getGroups().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String id) {
        return v -> {
            Intent intent = new Intent(localActivity, GroupActivity.class);
            intent.putExtra("id", id);
            localActivity.startActivity(intent);
        };
    }

}
