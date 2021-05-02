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
import com.example.piston.data.Post;
import com.example.piston.databinding.ItemPostBinding;

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
        viewModel.getPosts().observe(activity, cosa -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public GroupAdapter.GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_post, parent, false);
        return new GroupAdapter.GroupHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupHolder holder, int position) {
        Post post = Objects.requireNonNull(viewModel.getPosts().getValue()).get(position);
        holder.bind(post);
        Glide.with(localActivity)
                .load(post.getImageLink())
                .into(holder.binding.postPicture);
        holder.getBinding().postItemCard.setOnClickListener(openNewActivity(post.getDocumentID(), post.getId()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getPosts().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String documentID, String id) {
        return v -> {
            Intent intent = new Intent(localActivity, GroupActivity.class);
            intent.putExtra("collection", "groups");
            intent.putExtra("document", documentID);
            intent.putExtra("id", id);
            localActivity.startActivity(intent);
        };
    }

}