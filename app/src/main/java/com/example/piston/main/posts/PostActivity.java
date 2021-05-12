package com.example.piston.main.posts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityPostBinding;
import com.example.piston.main.global.category.CategoryActivity;
import com.example.piston.main.groups.group.GroupActivity;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.textwatchers.BaseTextWatcher;
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.google.android.material.textfield.TextInputLayout;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Objects;

public class PostActivity extends AppCompatActivity implements PostAdapter.PostAdapterListener {

    private PostViewModel viewModel;
    private ActivityPostBinding binding;
    private RecyclerView.SmoothScroller smoothScroller;
    private String collection;
    private String document;
    private boolean orphan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        collection = intent.getStringExtra("collection");
        document = intent.getStringExtra("document");
        orphan = intent.getBooleanExtra("orphan", false);
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

        binding.threadReplyButton.setOnClickListener(v -> replyPopUp(null, null, null));
        binding.heartButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                viewModel.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
                viewModel.setLiked(false);
            }
        });

        viewModel.getPostTitle().observe(this, binding.postsTopAppBar::setTitle);
        viewModel.getLoaded().observe(this, aBoolean -> {
            if (aBoolean && replyID != null) {
                goToReply(replyID);
            }
        });
        viewModel.getLiked().observe(this, aBoolean -> binding.heartButton.setLiked(aBoolean));
        viewModel.getPostImageLink().observe(this, imageLink -> Glide.with(this)
                .load(imageLink)
                .into(binding.postPicture));
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
    public void quoteOnClick(View v, String quoteID) {
        goToReply(quoteID);
    }

    @Override
    public void finish() {
        if (orphan) {
            Intent intent = new Intent(this, (collection.equals("groups")) ?
                    GroupActivity.class : CategoryActivity.class);
            intent.putExtra("id", document);
            startActivity(intent);
        }
        super.finish();
    }

    @Override
    public void replyPopUp(String owner, String content, String quoteID) {
        View popupView = getLayoutInflater().inflate(R.layout.pupup_reply, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // If you need the PopupWindow to dismiss when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popup_window_animation_slide);

        // Adjust popup window location when keyboard pops up
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        TextInputLayout textField = popupWindow.getContentView().findViewById(R.id.popup);

        textField.setEndIconVisible(false);
        textField.requestFocus();

        Objects.requireNonNull(textField.getEditText())
                .addTextChangedListener(new CounterWatcher(500, textField));
        textField.getEditText().addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                textField.setEndIconVisible(s.length() != 0);
            }
        });

        textField.setEndIconOnClickListener(v -> {
            if (owner == null || content == null || quoteID == null) {
                viewModel.createReply(textField.getEditText().getText().toString());
            } else {
                viewModel.createReply(textField.getEditText().getText().toString(), content, owner, quoteID);
            }
            popupWindow.dismiss();
        });
        // force show keyboard once pop up window is open
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        // dims background when popup window shows up
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);

        // restore dim
        popupWindow.setOnDismissListener(() -> {
            lp.alpha = 1f;
            getWindow().setAttributes(lp);
        });
    }
}
