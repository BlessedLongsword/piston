package com.example.piston.main.personal;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.data.Folder;
import com.example.piston.databinding.ItemFolderBinding;
import com.example.piston.main.personal.folder.FolderActivity;

import java.util.Objects;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.FolderHolder> {

    private final FragmentActivity localActivity;
    private final PersonalViewModel viewModel;

    public static class FolderHolder extends RecyclerView.ViewHolder {

        private final ItemFolderBinding binding;

        public FolderHolder(ItemFolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Folder item) {
            binding.setFolder(item);
        }

        public ItemFolderBinding getBinding() {
            return binding;
        }
    }

    public PersonalAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(PersonalViewModel.class);
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
        holder.getBinding().folderItemCard.setOnClickListener(openNewActivity(folder.getTitle()));
    }

    @Override
    public int getItemCount() {
        return viewModel.getFolders().getValue().size();
    }

    private View.OnClickListener openNewActivity(String id) {
        return v -> {
            Intent intent = new Intent(localActivity, FolderActivity.class);
            intent.putExtra("id", id);
            localActivity.startActivity(intent);
        };
    }

}
