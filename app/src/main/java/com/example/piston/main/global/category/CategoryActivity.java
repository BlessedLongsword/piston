package com.example.piston.main.global.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCategoryBinding;
import com.example.piston.main.global.category.info.CategoryInfoActivity;
import com.example.piston.main.posts.createPost.CreatePostActivity;
import com.example.piston.utilities.MyViewModelFactory;

public class CategoryActivity extends AppCompatActivity {

    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        title = intent.getStringExtra("id");

        CategoryViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory(title))
                .get(CategoryViewModel.class);
        ActivityCategoryBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_category);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.viewPostsTopAppBar.setTitle(title);
        binding.viewPostsTopAppBar.setNavigationOnClickListener((view) -> finish());
        binding.recyclerviewCategory.setAdapter(new CategoryAdapter(this));

    }

    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("collection", "categories");
        intent.putExtra("document", title);
        startActivity(intent);
    }

    public void goToInfo(View view) {
        Intent intent = new Intent(this, CategoryInfoActivity.class);
        intent.putExtra("document", title);
        startActivity(intent);
    }
}
