package com.example.piston.main.global.category.info;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityCategoryInfoBinding;
import com.example.piston.utilities.MyViewModelFactory;
import com.like.LikeButton;
import com.like.OnLikeListener;

public class CategoryInfoActivity extends AppCompatActivity {

    CategoryInfoViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_info);

        Intent intent = getIntent();
        String title = intent.getStringExtra("document");

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(title))
                .get(CategoryInfoViewModel.class);
        ActivityCategoryInfoBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_category_info);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.categoryInfoTopAppBar.setTitle(title);
        binding.categoryInfoTopAppBar.setNavigationOnClickListener((view) -> finish());

        binding.starButton.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                viewModel.setSub(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
                viewModel.setSub(false);
            }
        });

        viewModel.getImageLink().observe(this, aString -> Glide.with(this)
                .load(aString)
                .into(binding.categoryInfoImage));

        viewModel.getSubscribed().observe(this, aBoolean -> {
            if(aBoolean){
                binding.starButton.setLiked(true);
            }
        });
    }

}
