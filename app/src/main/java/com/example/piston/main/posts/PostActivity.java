package com.example.piston.main.posts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.piston.R;
import com.example.piston.databinding.ActivityPostBinding;
import com.example.piston.utilities.MyViewModelFactory;

import java.util.Objects;

public class PostActivity extends AppCompatActivity implements PostAdapter.PostAdapterListener {

    private PostViewModel viewModel;
    private ActivityPostBinding binding;
    private RecyclerView.SmoothScroller smoothScroller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        String collection = intent.getStringExtra("collection");
        String document = intent.getStringExtra("document");
        String postID = intent.getStringExtra("id");

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(collection, document, postID))
                .get(PostViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        smoothScroller = new LinearSmoothScroller(this) {
                    @Override
                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };

        binding.postsTopAppBar.setNavigationOnClickListener((view) -> finish());


        binding.recyclerviewPosts.setAdapter(new PostAdapter(this, this));

        viewModel.getPostTitle().observe(this, binding.postsTopAppBar::setTitle);
    }

    @Override
    public void quoteOnClick(View v, String quoteID) {
        for (int i = 1; i < Objects.requireNonNull(viewModel.getReplies().getValue()).size() + 1; i++) {
            if (viewModel.getReplies().getValue().get(i).getQuoteID().equals(quoteID)) {
                smoothScroller.setTargetPosition(i);
                Objects.requireNonNull(binding.recyclerviewPosts.getLayoutManager())
                        .startSmoothScroll(smoothScroller);
                break;
            }
        }
    }
}
