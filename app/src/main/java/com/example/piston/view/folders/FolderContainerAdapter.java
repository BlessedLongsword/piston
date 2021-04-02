package com.example.piston.view.folders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;

import java.util.ArrayList;

public class FolderContainerAdapter extends RecyclerView.Adapter<FolderContainerAdapter.FolderHolder>{

    private final ArrayList<String> localDataSet;
    private final Context parentContext;

    public static class FolderHolder extends RecyclerView.ViewHolder {

        private final LinearLayout containerLayout;
        private final TextView folderTitle;
        private final ImageView folderIcon;

        public FolderHolder(View view) {
            super(view);
            containerLayout = view.findViewById(R.id.card_layout);
            folderTitle = view.findViewById(R.id.card_title);
            folderIcon = view.findViewById(R.id.card_image);
        }

        public LinearLayout getContainerLayout() {
            return containerLayout;
        }

        public TextView getFolderTitle() {
            return folderTitle;
        }

        public ImageView getFolderIcon() {
            return folderIcon;
        }
    }

    public FolderContainerAdapter(Context current, ArrayList<String> dataSet) {
        parentContext = current;
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public FolderContainerAdapter.FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_item, parent, false);
        return new FolderContainerAdapter.FolderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderContainerAdapter.FolderHolder holder, int position) {
        holder.getFolderTitle().setText(localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        if (localDataSet != null)
            return localDataSet.size();
        return 0;
    }

}
