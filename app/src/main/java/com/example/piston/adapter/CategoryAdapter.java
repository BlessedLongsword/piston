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
import com.example.piston.viewmodel.GlobalFragmentViewModel;
import com.example.piston.viewmodel.PersonalFragmentViewModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private final FragmentActivity localActivity;
    private final GlobalFragmentViewModel viewModel;

    public static class CategoryHolder extends RecyclerView.ViewHolder {

        private final FrameLayout layout;
        private final TextView categoryTitle, categoryDesc;

        public CategoryHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.category_item_card);
            categoryTitle = view.findViewById(R.id.category_title);
            categoryDesc = view.findViewById(R.id.category_desc);
        }

        public TextView getCategoryTitle() {
            return categoryTitle;
        }

        public TextView getCategoryDesc() {
            return categoryDesc;
        }

        public FrameLayout getLayout() {
            return layout;
        }
    }

    public CategoryAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GlobalFragmentViewModel.class);
        viewModel.getCategories().observe(activity, cosa -> {
            Log.d("nowaybro", "Updatedadapter");
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_item, viewGroup, false);
        return new CategoryAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryHolder categoryHolder, int position) {
        categoryHolder.getCategoryTitle().setText(viewModel.getCategories().getValue().get(position).getTitle());
        categoryHolder.getCategoryDesc().setText(viewModel.getCategories().getValue().get(position).getDescription());
        categoryHolder.getLayout().setOnClickListener(view -> {
            Intent intent = new Intent(localActivity, ViewPostsActivity.class);
            intent.putExtra("title", viewModel.getCategories().getValue().get(position).getTitle());
            intent.putExtra("description", viewModel.getCategories().getValue().get(position).getDescription());
            localActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
    Log.d("nowaybro","hh");
    return viewModel.getCategories().getValue().size();
    }

}
