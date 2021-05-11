package com.example.piston.main.posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.databinding.ActivityPostBinding;
import com.example.piston.main.global.category.CategoryActivity;
import com.example.piston.main.groups.group.GroupActivity;
import com.example.piston.utilities.MyViewModelFactory;

import java.util.Objects;

public class PostActivity extends AppCompatActivity implements PostAdapter.PostAdapterListener {

    private PostViewModel viewModel;
    private ActivityPostBinding binding;
    private RecyclerView.SmoothScroller smoothScroller;
    private String collection;
    private String document;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        collection = intent.getStringExtra("collection");
        document = intent.getStringExtra("document");
        String postID = intent.getStringExtra("id");
        String replyID = intent.getStringExtra("reply");

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
        viewModel.getLoaded().observe(this, aBoolean -> {
            if (aBoolean && replyID != null) {
                goToReply(replyID);
            }
        });
    }

    @Override
    public void quoteOnClick(View v, String quoteID) {
        goToReply(quoteID);
    }

    public void goToReply(String replyID) {
        for (int i = 0; i < Objects.requireNonNull(viewModel.getReplies().getValue()).size(); i++) {
            if (viewModel.getReplies().getValue().get(i).getId().equals(replyID)) {
                smoothScroller.setTargetPosition(i+1);
                Objects.requireNonNull(binding.recyclerviewPosts.getLayoutManager())
                        .startSmoothScroll(smoothScroller);
                break;
            }
        }
    }

    @Override
    public void finish() {
        if (getParentActivityIntent() == null) {
            Intent intent;
            if (collection.equals("groups")) {
                intent = new Intent(this, GroupActivity.class);
            } else {
                intent = new Intent(this, CategoryActivity.class);
            }
            intent.putExtra("id", document);
            startActivity(intent);
        }
        super.finish();
    }
}
