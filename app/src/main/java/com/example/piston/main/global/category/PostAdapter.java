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

import com.example.piston.R;
import com.example.piston.data.Post;
import com.example.piston.databinding.ItemPostBinding;
import com.example.piston.main.posts.PostActivity;

import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

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

    public PostAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(CategoryViewModel.class);
        viewModel.getPosts().observe(activity, cosa -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_post, parent, false);
        return new PostAdapter.PostHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {
        Post post = Objects.requireNonNull(viewModel.getPosts().getValue()).get(position);
        holder.bind(post);
        holder.getBinding().postItemCard.setOnClickListener(openNewActivity(post.getTitle()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getPosts().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String id) {
        return v -> {
            Intent intent = new Intent(localActivity, PostActivity.class);
            intent.putExtra("id", id);
            localActivity.startActivity(intent);
        };
    }

}
