package com.example.piston.view.folders;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.view.user.ViewProfileActivity;

import java.util.ArrayList;

public class FolderContainerAdapter extends RecyclerView.Adapter<FolderContainerAdapter.FolderHolder>{

    private final ArrayList<String> localDataSet;
    private final FragmentActivity localActivity;

    public static class FolderHolder extends RecyclerView.ViewHolder {

        private final FrameLayout layout;
        private final TextView folderTitle;

        public FolderHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.folder_item_layout);
            folderTitle = view.findViewById(R.id.folder_item_title);
        }

        public TextView getFolderTitle() {
            return folderTitle;
        }

        public FrameLayout getLayout() {
            return layout;
        }
    }

    public FolderContainerAdapter(FragmentActivity activity, ArrayList<String> dataSet) {
        localActivity = activity;
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.folder_item, viewGroup, false);
        return new FolderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder folderHolder, int position) {
        folderHolder.getFolderTitle().setText(localDataSet.get(position));
        folderHolder.getLayout().setOnClickListener(view -> {
            Intent intent = new Intent(localActivity, ViewProfileActivity.class);
            localActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (localDataSet != null)
            return localDataSet.size();
        return 0;
    }

}
