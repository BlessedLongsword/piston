package com.example.piston.main.groups.group.info;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.data.users.GroupMember;
import com.example.piston.databinding.ItemMemberBinding;
import com.example.piston.main.profile.ProfileActivity;
import com.example.piston.utilities.Values;

import java.util.Objects;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {

    private final FragmentActivity localActivity;
    private final GroupInfoViewModel viewModel;
    private int position;

    public static class MemberHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{

        private final ItemMemberBinding binding;
        private final int currentUserPriority;

        public MemberHolder(ItemMemberBinding binding, int currentUserPriority) {
            super(binding.getRoot());
            this.binding = binding;
            this.currentUserPriority = currentUserPriority;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        public void bind(GroupMember item) { binding.setMember(item); }

        public ItemMemberBinding getBinding() { return binding; }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (binding.getMember().getPriority() == 2) {
                if (currentUserPriority < 2) {
                    menu.add(Menu.NONE, R.id.ctx_menu_members_kick, Menu.NONE, R.string.kick_from_group);
                    if (currentUserPriority < 1)
                        menu.add(Menu.NONE, R.id.ctx_menu_members_mod, Menu.NONE, R.string.make_moderator);
                }
            }
            if (binding.getMember().getPriority() == 1 && currentUserPriority < 1) {
                menu.add(Menu.NONE, R.id.ctx_menu_members_dismiss_mod, Menu.NONE, R.string.dismiss_as_moderator);
            }
        }
    }

    public MemberAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GroupInfoViewModel.class);
        viewModel.getMembers().observe(activity, item -> notifyDataSetChanged());
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemViewType(int position) {
        return Objects.requireNonNull(viewModel.getMembers().getValue()).get(position).getPriority();
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMemberBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_member, parent, false);
        if (viewType == GroupMember.OWNER) {
            binding.groupOwnerIcon.setVisibility(View.VISIBLE);
        } else if (viewType == GroupMember.MOD) {
            binding.groupModeratorIcon.setVisibility(View.VISIBLE);
        }
        return new MemberHolder(binding, Objects.requireNonNull(viewModel.getPriority().getValue()));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.MemberHolder holder, int position) {
        GroupMember member = Objects.requireNonNull(viewModel.getMembers().getValue()).get(position);  //member és null ------------------------------------------------
        holder.bind(member);

        Glide.with(localActivity)
                .load(member.getProfilePictureLink())
                .placeholder(R.drawable.default_profile_picture)
                .into(holder.binding.memberProfilePicture);

        holder.getBinding().memberCard.setOnClickListener(openNewActivity(member.getEmail()));
        holder.getBinding().memberCard.setOnLongClickListener(v -> {
            setPosition(position);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getMembers().getValue()).size();
    }

    @Override
    public void onViewRecycled(@NonNull MemberAdapter.MemberHolder holder) {
        holder.getBinding().memberCard.setOnLongClickListener(null);
        holder.getBinding().memberCard.setOnClickListener(null);
        super.onViewRecycled(holder);
    }

    private View.OnClickListener openNewActivity(String email) {
        return v -> {
            Intent intent = new Intent(localActivity, ProfileActivity.class);
            intent.putExtra(Values.EMAIL, email);
            localActivity.startActivity(intent);
        };
    }
}
