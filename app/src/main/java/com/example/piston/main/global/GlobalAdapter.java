package com.example.piston.main.global;

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
import com.example.piston.data.sections.Category;
import com.example.piston.databinding.ItemCategoryBinding;
import com.example.piston.main.global.category.CategoryActivity;
import com.example.piston.utilities.Values;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Objects;

public class GlobalAdapter extends RecyclerView.Adapter<GlobalAdapter.CategoryHolder> {

    private final FragmentActivity localActivity;
    private final GlobalViewModel viewModel;

    public static class CategoryHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding binding;

        public CategoryHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category item) {
            binding.setCategory(item);
        }

        public ItemCategoryBinding getBinding() {
            return binding;
        }
    }

    public GlobalAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GlobalViewModel.class);
        viewModel.getCategories().observe(activity, item -> notifyDataSetChanged());
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
    public void onBindViewHolder(@NonNull GlobalAdapter.CategoryHolder holder, int position) {
        Category category = Objects.requireNonNull(viewModel.getCategories().getValue()).get(position);
        holder.bind(category);
        if (category != null) {
            Glide.with(localActivity)
                    .load(category.getImageLink())
                    .into(holder.binding.categoryImage);
            holder.getBinding().categoryItemCard.setOnClickListener(openNewActivity(category.getId()));
        }
        holder.binding.starButton.setLiked(Objects.requireNonNull(viewModel.getSubscribed()
                .getValue()).get(position));
        holder.getBinding().starButton.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                viewModel.setSub(true, Objects.requireNonNull(category).getId());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
                viewModel.setSub(false, Objects.requireNonNull(category).getId());
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull GlobalAdapter.CategoryHolder holder) {
        holder.getBinding().categoryItemCard.setOnClickListener(null);
        holder.getBinding().starButton.setOnLikeListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getCategories().getValue()).size();
    }

    private View.OnClickListener openNewActivity(String categoryID) {
        return v -> {
            Intent intent = new Intent(localActivity, CategoryActivity.class);
            intent.putExtra(Values.SECTION_ID, categoryID);
            intent.putExtra(Values.IS_ADMIN, viewModel.getIsAdmin().getValue());
            localActivity.startActivity(intent);
        };
    }
}
