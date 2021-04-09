package com.example.piston.adapter;

import android.content.Intent;
import android.util.Log;
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
import com.example.piston.view.posts.ViewPostsActivity;
import com.example.piston.viewmodel.PersonalFragmentViewModel;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderHolder> {

    private final FragmentActivity localActivity;
    private final PersonalFragmentViewModel viewModel;

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

    public FolderAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(PersonalFragmentViewModel.class);
        viewModel.getFolders().observe(activity, cosa -> {
            Log.d("nowaybro", "Updated");
            notifyDataSetChanged();
        });
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
        folderHolder.getFolderTitle().setText(viewModel.getFolders().getValue().get(position));
        folderHolder.getLayout().setOnClickListener(view -> {
            Intent intent = new Intent(localActivity, ViewPostsActivity.class);
            intent.putExtra("title", viewModel.getFolders().getValue().get(position));
            localActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return viewModel.getFolders().getValue().size();
    }

}
