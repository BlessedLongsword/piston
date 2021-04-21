package com.example.piston.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.data.Category;
import com.example.piston.databinding.ItemCategoryBinding;
import com.example.piston.viewmodels.GlobalFragmentViewModel;

import java.util.Objects;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private final FragmentActivity localActivity;
    private final GlobalFragmentViewModel viewModel;

    public static class CategoryHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding binding;

        public CategoryHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category item) {
            binding.setCategory(item);
            binding.executePendingBindings();
        }
    }

    public CategoryAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GlobalFragmentViewModel.class);
        viewModel.getCategories().observe(activity, cosa -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCategoryBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_category, parent, false);
        return new CategoryHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryHolder holder, int position) {
        Category category = Objects.requireNonNull(viewModel.getCategories().getValue()).get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return viewModel.getCategories().getValue().size();
    }

}
