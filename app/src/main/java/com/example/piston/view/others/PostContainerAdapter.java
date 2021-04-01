package com.example.piston.view.others;

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

        private final LinearLayout containerLayout;
        private final LinearLayout cardLayout;
        private final LinearLayout textLayout;
        private final CardView cardView;
        private final ImageView postPicture;
        private final TextView postTitle;
        private final TextView postContent;

        public PostHolder(View view) {
            super(view);
            containerLayout = view.findViewById(R.id.layout);
            cardLayout = view.findViewById(R.id.card_layout);
            textLayout = view.findViewById(R.id.text_layout);
            cardView = view.findViewById(R.id.card_view);
            postPicture = view.findViewById(R.id.card_image);
            postTitle = view.findViewById(R.id.card_title);
            postContent = view.findViewById(R.id.card_content);
        }

        public LinearLayout getContainerLayout() {
            return containerLayout;
        }

        public LinearLayout getCardLayout() {
            return cardLayout;
        }

        public LinearLayout getTextLayout() {
            return textLayout;
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getPostPicture() {
            return postPicture;
        }

        public TextView getPostTitle() {
            return postTitle;
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
