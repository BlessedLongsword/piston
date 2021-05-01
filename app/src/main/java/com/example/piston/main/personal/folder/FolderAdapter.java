package com.example.piston.main.personal.folder;

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

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.NoteHolder> {

    private final FragmentActivity localActivity;
    private final FolderViewModel viewModel;

    public static class NoteHolder extends RecyclerView.ViewHolder {

        private final ItemPostBinding binding;

        public NoteHolder(ItemPostBinding binding) {
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

    public FolderAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(FolderViewModel.class);
        viewModel.getPosts().observe(activity, cosa -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public FolderAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_post, parent, false);
        return new FolderAdapter.NoteHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.NoteHolder holder, int position) {
        Post post = Objects.requireNonNull(viewModel.getPosts().getValue()).get(position);
        holder.bind(post);
        Glide.with(localActivity)
                .load(post.getImageLink())
                .into(holder.binding.postPicture);
        holder.getBinding().postItemCard.setOnClickListener(openNewActivity(post.getTitle()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getPosts().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String id) {
        return v -> {
            Intent intent = new Intent(localActivity, FolderActivity.class);
            intent.putExtra("id", id);
            localActivity.startActivity(intent);
        };
    }

}

