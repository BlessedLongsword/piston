package com.example.piston.main.global.category;

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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.PostHolder> {

    private final FragmentActivity localActivity;
    private final CategoryViewModel viewModel;

    public static class PostHolder extends RecyclerView.ViewHolder {

        private final ItemPostBinding binding;

        public PostHolder(ItemPostBinding binding) {
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

    public CategoryAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(CategoryViewModel.class);
        viewModel.getPosts().observe(activity, posts -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public CategoryAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_post, parent, false);
        return new CategoryAdapter.PostHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.PostHolder holder, int position) {
        Post post = Objects.requireNonNull(viewModel.getPosts().getValue()).get(position);
        holder.bind(post);

        if(post.getProfileImageLink() != null ) {
            Glide.with(localActivity)
                    .load(post.getProfileImageLink())
                    .into(holder.binding.postItemProfilePicture);
        }

        if (post.getImageLink() != null) {
            Glide.with(localActivity)
                    .load(post.getImageLink())
                    .into(holder.binding.postPicture);
        } else
            holder.getBinding().postPicture.setVisibility(View.GONE);
        holder.getBinding().postItemCard.setOnClickListener(openNewActivity(post.getSectionID(), post.getId()));
        holder.getBinding().userProfile.setOnClickListener(openProfile(post.getOwnerEmail()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getPosts().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String sectionID, String postID) {
        return v -> {
            Intent intent = new Intent(localActivity, PostActivity.class);
            intent.putExtra(Values.SCOPE, Values.GLOBAL);
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
