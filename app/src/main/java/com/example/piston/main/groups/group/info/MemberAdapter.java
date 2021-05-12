package com.example.piston.main.groups.group.info;

import android.content.Intent;
import android.util.Log;
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
import com.example.piston.data.GroupMember;
import com.example.piston.databinding.ItemMemberBinding;
import com.example.piston.main.profile.ProfileActivity;

import java.util.Objects;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {

    private final FragmentActivity localActivity;
    private final GroupInfoViewModel viewModel;

    public static class MemberHolder extends RecyclerView.ViewHolder {

        private final ItemMemberBinding binding;

        public MemberHolder(ItemMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GroupMember item) { binding.setMember(item); }

        public ItemMemberBinding getBinding() { return binding; }

    }

    public MemberAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GroupInfoViewModel.class);
        viewModel.getMembers().observe(activity, item -> {
            Log.d("DBReadTAG", "jajsi");
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMemberBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_member, parent, false);
        return new MemberHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.MemberHolder holder, int position) {
        GroupMember member = Objects.requireNonNull(viewModel.getMembers().getValue()).get(position);
        holder.bind(member);
        Glide.with(localActivity)
                .load(member.getProfilePictureLink())
                .into(holder.binding.memberProfilePicture);
        holder.getBinding().memberCard.setOnClickListener(openNewActivity(member.getEmail()));
        //holder.getBinding().memberCard.setOnLongClickListener();
    }

    @Override
    public int getItemCount() {
        Log.d("DBReadTAG", String.valueOf(Objects.requireNonNull(viewModel.getMembers().getValue()).size()));
        return Objects.requireNonNull(viewModel.getMembers().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String email) {
        return v -> {
            Intent intent = new Intent(localActivity, ProfileActivity.class);
            intent.putExtra("email", email);
            localActivity.startActivity(intent);
        };
    }

    //private View.OnLongClickListener
}
