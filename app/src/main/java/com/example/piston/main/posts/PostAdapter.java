package com.example.piston.main.posts;

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
import com.example.piston.data.Post;
import com.example.piston.databinding.ItemReplyBinding;
import com.example.piston.databinding.ItemThreadBinding;

import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final FragmentActivity localActivity;
    private final PostViewModel viewModel;

    public static class ThreadHolder extends RecyclerView.ViewHolder {

        private final ItemThreadBinding binding;

        public ThreadHolder(ItemThreadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Post item) {
            binding.setThread(item);
        }

        public ItemThreadBinding getBinding() {
            return binding;
        }
    }

    public static class ReplyHolder extends RecyclerView.ViewHolder {

        private final ItemReplyBinding binding;

        public ReplyHolder(ItemReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Post item) {
            binding.setReply(item);
        }

        public ItemReplyBinding getBinding() {
            return binding;
        }
    }

    public PostAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(PostViewModel.class);
        viewModel.getPosts().observe(activity, cosa -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0){
            Log.d("DBReadTAG", "if else entrado");
            ItemThreadBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_thread, parent, false);
            return new PostAdapter.ThreadHolder(binding);
        }
        else {
            ItemReplyBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_reply, parent, false);
            return new PostAdapter.ReplyHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = Objects.requireNonNull(viewModel.getPosts().getValue()).get(position);
        Log.d("DBReadTAG", "pre-if");
        if (holder.getItemViewType() == 0) {
            Log.d("DBReadTAG", "lo estoy intentando ;_;");
            PostAdapter.ThreadHolder hold  = (PostAdapter.ThreadHolder) holder;
            hold.bind(post);
            Glide.with(localActivity)
                    .load(post.getImageLink())
                    .into(hold.binding.postPicture);
        }
        else {
            PostAdapter.ReplyHolder hold = ((PostAdapter.ReplyHolder) holder);
            hold.bind(post);
        }
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getPosts().getValue()).size();
    }
}

