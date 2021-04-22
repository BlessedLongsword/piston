package com.example.piston.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.data.Category;
import com.example.piston.data.Folder;
import com.example.piston.databinding.ItemCategoryBinding;
import com.example.piston.databinding.ItemFolderBinding;
import com.example.piston.views.posts.ViewPostsActivity;
import com.example.piston.viewmodels.PersonalFragmentViewModel;

import java.util.Objects;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderHolder> {

    private final FragmentActivity localActivity;
    private final PersonalFragmentViewModel viewModel;

    public static class FolderHolder extends RecyclerView.ViewHolder {

        private final ItemFolderBinding binding;

        public FolderHolder(ItemFolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Folder item) {
            binding.setFolder(item);
            //binding.executePendingBindings();
        }
    }

    public FolderAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(PersonalFragmentViewModel.class);
        viewModel.getFolders().observe(activity, cosa -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFolderBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_folder, parent, false);
        return new FolderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        Folder folder = Objects.requireNonNull(viewModel.getFolders().getValue()).get(position);
        holder.bind(folder);
    }

    @Override
    public int getItemCount() {
        return viewModel.getFolders().getValue().size();
    }

}
