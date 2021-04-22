package com.example.piston.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.viewmodels.ViewPostsActivityViewModel;
import com.example.piston.views.posts.ViewPostsActivity;

public class ViewPostsAdapter extends RecyclerView.Adapter<ViewPostsAdapter.PostsHolder> {

    private final FragmentActivity localActivity;
    private final ViewPostsActivityViewModel viewModel;

    public static class PostsHolder extends RecyclerView.ViewHolder {

        private final FrameLayout layout;
        private final TextView postTitle, postOwner, postContent;

        public PostsHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.card);
            postTitle = view.findViewById(R.id.post_title);
            postOwner = view.findViewById(R.id.post_owner);
            postContent = view.findViewById(R.id.post_content);
        }

        public TextView getPostTitle() {
            return postTitle;
        }

        public TextView getPostOwner() {
            return postOwner;
        }

        public TextView getPostContent() {
            return postContent;
        }

        public FrameLayout getLayout() {
            return layout;
        }
    }

    public ViewPostsAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(ViewPostsActivityViewModel.class);
        viewModel.getPosts().observe(activity, cosa -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_category, viewGroup, false);
        return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPostsAdapter.PostsHolder postsHolder, int position) {
        postsHolder.getPostTitle().setText(viewModel.getPosts().getValue().get(position).getTitle());
        postsHolder.getPostOwner().setText(viewModel.getPosts().getValue().get(position).getOwner());
        postsHolder.getPostContent().setText(viewModel.getPosts().getValue().get(position).getContent());
        postsHolder.getLayout().setOnClickListener(view -> {
            Intent intent = new Intent(localActivity, ViewPostsActivity.class);
            intent.putExtra("title", viewModel.getPosts().getValue().get(position).getTitle());
            intent.putExtra("owner", viewModel.getPosts().getValue().get(position).getOwner());
            intent.putExtra("content", viewModel.getPosts().getValue().get(position).getContent());
            localActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return viewModel.getPosts().getValue().size();
    }

}