package com.example.piston.main.groups.group;

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
import com.example.piston.data.posts.Post;
import com.example.piston.databinding.ItemPostBinding;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.main.profile.ProfileActivity;
import com.example.piston.utilities.Values;

import java.util.Objects;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {

    private final FragmentActivity localActivity;
    private final GroupViewModel viewModel;

    public static class GroupHolder extends RecyclerView.ViewHolder {

        private final ItemPostBinding binding;

        public GroupHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Post item) {
            binding.setPost(item);
        }

        public ItemPostBinding getBinding() {
            return binding;
        }
    }

    public GroupAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GroupViewModel.class);
        viewModel.getPosts().observe(activity, posts -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        Post post = Objects.requireNonNull(viewModel.getPosts().getValue()).get(position);
        if (post.getImageLink() == null) {
            if (post.getPinned())
                return 2;
            else return 0;
        } else {
            if (post.getPinned())
                return 3;
            else
                return 1;
        }
    }

    @NonNull
    @Override
    public GroupAdapter.GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_post, parent, false);

        if (viewType == 0 || viewType == 2)
            binding.postPicture.setVisibility(View.GONE);

        if (!(viewType == 2 || viewType == 3)) {
            Objects.requireNonNull(binding.extraInfoLayout).setVisibility(View.GONE);
        }

        Objects.requireNonNull(binding.heartIcon).setVisibility(View.GONE);
        binding.heartCount.setVisibility(View.GONE);

        return new GroupAdapter.GroupHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupHolder holder, int position) {
        Post post = Objects.requireNonNull(viewModel.getPosts().getValue()).get(position);
        holder.bind(post);

        Glide.with(localActivity)
                .load(post.getProfileImageLink())
                .placeholder(R.drawable.default_profile_picture)
                .into(holder.binding.postItemProfilePicture);

        if (post.getImageLink() != null)
            Glide.with(localActivity)
                    .load(post.getImageLink())
                    .into(holder.binding.postPicture);

        holder.getBinding().postItemCard.setOnClickListener(openNewActivity(post.getSectionID(), post.getId()));
        holder.getBinding().userProfile.setOnClickListener(openProfile(post.getOwnerEmail()));
    }

    @Override
    public void onViewRecycled(@NonNull GroupAdapter.GroupHolder holder) {
        holder.getBinding().postItemCard.setOnClickListener(null);
        holder.getBinding().userProfile.setOnClickListener(null);

        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getPosts().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String sectionID, String postID) {
        return v -> {
            Intent intent = new Intent(localActivity, PostActivity.class);
            intent.putExtra(Values.SCOPE, Values.GROUPS);
            intent.putExtra(Values.SECTION_ID, sectionID);
            intent.putExtra(Values.POST_ID, postID);
            localActivity.startActivity(intent);
        };
    }

    private View.OnClickListener openProfile(String email) {
        return v -> {
            Intent intent = new Intent(localActivity, ProfileActivity.class);
            intent.putExtra(Values.EMAIL, email);
            localActivity.startActivity(intent);
        };
    }

}