package com.example.piston.main.global.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCategoryBinding;
import com.example.piston.main.MainActivity;
import com.example.piston.main.global.category.info.CategoryInfoActivity;
import com.example.piston.main.notifications.NotificationsActivity;
import com.example.piston.main.posts.createPost.CreatePostActivity;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.Values;

public class CategoryActivity extends AppCompatActivity {

    private String categoryID;
    private boolean orphan;
    private boolean postDidNotExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        categoryID = intent.getStringExtra(Values.SECTION_ID);

        orphan = intent.getBooleanExtra("orphan", false);

        postDidNotExist = intent.getBooleanExtra("postDidNotExist", false);

        if (postDidNotExist) {
            orphan = false;
            finish();
        }

        CategoryViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory(categoryID))
                .get(CategoryViewModel.class);
        ActivityCategoryBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_category);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getTitle().observe(this, binding.viewPostsTopAppBar::setTitle);
        binding.viewPostsTopAppBar.setNavigationOnClickListener((view) -> finish());
        binding.recyclerviewCategory.setAdapter(new CategoryAdapter(this));

    }

    @SuppressWarnings("unused")
    public void createPost(View view) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra(Values.SCOPE, Values.GLOBAL);
        intent.putExtra(Values.SECTION_ID, categoryID);
        startActivity(intent);
    }

    @Override
    public void finish() {
        if (orphan) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("tab", 2);
            startActivity(intent);
        }
        if (postDidNotExist) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
        }
        super.finish();
    }

    @SuppressWarnings("unused")
    public void goToInfo(View view) {
        goToInfo();
    }

    @SuppressWarnings("unused")
    public void goToInfo(MenuItem item) {
        goToInfo();
    }

    private void goToInfo() {
        Intent intent = new Intent(this, CategoryInfoActivity.class);
        intent.putExtra(Values.SECTION_ID, categoryID);
        intent.putExtra(Values.IS_ADMIN, getIntent().getBooleanExtra(Values.IS_ADMIN, false));
        startActivityForResult(intent, Values.DELETE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Values.DELETE_CODE)
            finish();
    }
}
