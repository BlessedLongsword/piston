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
import com.example.piston.data.posts.Post;
import com.example.piston.databinding.ItemPostBinding;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.utilities.Values;

import java.util.Objects;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.NoteHolder> {

    private final FragmentActivity localActivity;
    private final FolderViewModel viewModel;

    public static class NoteHolder extends RecyclerView.ViewHolder {

        private final ItemPostBinding binding;

        public NoteHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            Objects.requireNonNull(binding.postItemProfilePictureCard).setVisibility(View.GONE);
            binding.postOwner.setVisibility(View.GONE);
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
    public FolderAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPostBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_post, parent, false);

        if (viewType == 0 || viewType == 2)
            binding.postPicture.setVisibility(View.GONE);
        Objects.requireNonNull(binding.heartIcon).setVisibility(View.GONE);
        binding.heartCount.setVisibility(View.GONE);

        if (!(viewType == 2 || viewType == 3)) {
            Objects.requireNonNull(binding.extraInfoLayout).setVisibility(View.GONE);
        }

        return new FolderAdapter.NoteHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.NoteHolder holder, int position) {
        Post post = Objects.requireNonNull(viewModel.getPosts().getValue()).get(position);
        holder.bind(post);

        if (post.getImageLink() != null)
            Glide.with(localActivity)
                    .load(post.getImageLink())
                    .into(holder.binding.postPicture);

        holder.getBinding().postItemCard.setOnClickListener(openNewActivity(post.getSectionID(), post.getId()));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getPosts().getValue()).size();
    }

    @Override
    public void onViewRecycled(@NonNull FolderAdapter.NoteHolder holder) {
        holder.getBinding().postItemCard.setOnClickListener(null);
        super.onViewRecycled(holder);
    }


    private View.OnClickListener openNewActivity(String sectionID, String postID) {
        return v -> {
            Intent intent = new Intent(localActivity, PostActivity.class);
            intent.putExtra(Values.SCOPE, Values.PERSONAL);
            intent.putExtra(Values.SECTION_ID, sectionID);
            intent.putExtra(Values.POST_ID, postID);
            localActivity.startActivity(intent);
        };
    }

}

