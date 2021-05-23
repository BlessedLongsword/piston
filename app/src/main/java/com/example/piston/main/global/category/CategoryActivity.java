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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {

    private String categoryID;
    private boolean orphan;
    private boolean postDidNotExist;
    private CategoryViewModel viewModel;

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

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(categoryID))
                .get(CategoryViewModel.class);
        ActivityCategoryBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_category);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getTitle().observe(this, binding.viewPostsTopAppBar::setTitle);
        viewModel.getFilter().observe(this, s -> {
            switch (s) {
                case (Values.FILTER_MOST_LIKED):
                    binding.filterFieldText.setText(R.string.filter_most_liked);
                    break;
                case (Values.FILTER_ALPHABETICALLY):
                    binding.filterFieldText.setText(R.string.filter_alphabetically);
                    break;
                default:
                    binding.filterFieldText.setText(R.string.filter_default);
                    break;
            }
        });
        binding.filterField.setOnClickListener(chooseFilter());
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

    private View.OnClickListener chooseFilter() {
        return v -> new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.filter_by))
                .setItems(R.array.category_filters, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            updateFilter(Values.FILTER_DEFAULT, false);
                            break;
                        case 1:
                            updateFilter(Values.FILTER_MOST_LIKED, true);
                            break;
                        case 2:
                            updateFilter(Values.FILTER_ALPHABETICALLY, false);
                            break;
                    }
                }).show();
    }

    private void updateFilter(String filter, boolean descending) {
        viewModel.updateFilter(filter, descending);
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
        startActivityForResult(intent, Values.DELETE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Values.DELETE_CODE)
            finish();
    }
}
