package com.example.piston.view.posts;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.model.Post;

import java.util.ArrayList;

public class PostContainerAdapter extends RecyclerView.Adapter<PostContainerAdapter.PostHolder> {

    private final ArrayList<Post> localDataSet;
    private final Context parentContext;

    public static class PostHolder extends RecyclerView.ViewHolder {

        private final ImageView postPicture;
        private final TextView postTitle;
        private final TextView postOwner;
        private final TextView postContent;

        public PostHolder(View view) {
            super(view);
            postPicture = view.findViewById(R.id.post_picture);
            postTitle = view.findViewById(R.id.post_title);
            postOwner = view.findViewById(R.id.post_owner);
            postContent = view.findViewById(R.id.post_content);
        }


        public ImageView getPostPicture() {
            return postPicture;
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
    }

    public PostContainerAdapter(Context current, ArrayList<Post> dataSet) {
        parentContext = current;
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.getPostTitle().setText(localDataSet.get(position).getTitle());
        holder.getPostContent().setText(localDataSet.get(position).getContent());
        holder.getPostPicture().setImageBitmap(BitmapFactory
                .decodeFile(localDataSet.get(position).getPicturePath()));
    }

    @Override
    public int getItemCount() {
        if (localDataSet != null)
            return localDataSet.size();
        return 0;
    }

}
