package com.example.piston.main.posts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.data.posts.Reply;
import com.example.piston.databinding.ActivityPostBinding;
import com.example.piston.main.global.category.CategoryActivity;
import com.example.piston.main.groups.group.GroupActivity;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.Values;
import com.example.piston.utilities.textwatchers.BaseTextWatcher;
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.google.android.material.textfield.TextInputLayout;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Objects;

public class PostActivity extends AppCompatActivity implements PostAdapter.PostAdapterListener {

    private PostViewModel viewModel;
    private ActivityPostBinding binding;
    private String scope;
    private String sectionID;
    private String postID;
    private boolean orphan;
    private boolean postDoesNotExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        scope = intent.getStringExtra(Values.SCOPE);
        sectionID = intent.getStringExtra(Values.SECTION_ID);
        orphan = intent.getBooleanExtra(Values.ORPHAN, false);
        postID = intent.getStringExtra(Values.POST_ID);
        String replyID = intent.getStringExtra(Values.REPLY_ID);

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(scope, sectionID, postID))
                .get(PostViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.postsTopAppBar.setNavigationOnClickListener((view) -> finish());
        viewModel.getPriority().observe(this, priority -> {
                binding.postsTopAppBar.getMenu().getItem(0).setVisible(priority <= 1);
                binding.postsTopAppBar.getMenu().getItem(1).setVisible(priority == 0);
            });
        binding.recyclerviewPosts.setAdapter(new PostAdapter(this, this));

        registerForContextMenu(binding.recyclerviewPosts);

        binding.threadReplyButton.setOnClickListener(v -> replyPopUp(null, null, null, false));
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

        if (scope.equals("folders")){
            binding.heartButton.setVisibility(View.GONE);
            binding.shareButton.setVisibility(View.GONE);
        }

        viewModel.getPostTitle().observe(this, binding.postsTopAppBar::setTitle);
        viewModel.getLiked().observe(this, aBoolean -> binding.heartButton.setLiked(aBoolean));

        viewModel.getPostImageLink().observe(this, imageLink -> Glide.with(this)
                .load(imageLink)
                .into(binding.postPicture));

        viewModel.getProfileImageLink().observe(this, profileImageLink -> Glide.with(this)
                .load(profileImageLink)
                .into(binding.postProfilePicture));

        viewModel.getLoaded().observe(this, aBoolean -> {
            if (aBoolean && replyID != null)
                scrollTo(getItemPositionByID(replyID));
        });
        viewModel.getPostDoesNotExist().observe(this, aBoolean -> {
            postDoesNotExist = aBoolean;
            if (aBoolean) {
                Toast toast = Toast.makeText(this, R.string.post_does_not_exist, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
                finish();
            }
        });
    }

    public void goToReply(String replyID) {
        Log.d("nowaybro", "Going to replyID: " + replyID);
        View view = binding.recyclerviewPosts.findViewWithTag(replyID);
        float y = binding.recyclerviewPosts.getY() + view.getY();
        binding.postScrollView.smoothScrollTo(0, (int) y);
    }

    public void scrollTo(int itemPosition) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        for (int i = 0; i < itemPosition; i++) {
                            Log.d("DBReadTAG", String.valueOf(i));
                            goToReply(Objects.requireNonNull(viewModel.getReplies().getValue()).get(i).getId());
                        }
                    }
                },  1000);
    }

    public int getItemPositionByID(String replyID) {
        for (int i = 0; i < Objects.requireNonNull(viewModel.getReplies().getValue()).size(); i++) {
            if (viewModel.getReplies().getValue().get(i).getId().equals(replyID))
                return i;
        }
        return -1;
    }

    public void deletePost(MenuItem menuItem) {
        viewModel.deletePost();
        finish();
    }

    public void editPost(MenuItem menuItem) {
        Toast toast = Toast.makeText(getApplicationContext(), "Not implemented yet", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }

    public void sharePost(View v) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.piston.com/" + scope + "/" +
                sectionID + "/" + postID);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_post)));
    }

    @Override
    public void quoteOnClick(View v, String quoteID) {
        goToReply(quoteID);
    }

    @Override
    public void finish() {
        if (orphan) {
            Intent intent = new Intent(this, (scope.equals(Values.GROUPS)) ?
                    GroupActivity.class : CategoryActivity.class);
            intent.putExtra(Values.SECTION_ID, sectionID);
            intent.putExtra(Values.POST_DOES_NOT_EXIST, postDoesNotExist);
            startActivity(intent);
        }
        super.finish();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        int position = -1;

        try {
            position = ((PostAdapter) Objects.requireNonNull(binding.recyclerviewPosts
                    .getAdapter())).getPosition();
        } catch (Exception e) {
            Log.w("DBReadTAG", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }

        Reply reply = Objects.requireNonNull(viewModel.getReplies().getValue()).get(position);

        if (item.getItemId() == R.id.ctx_menu_reply_edit) {
            replyPopUp(reply.getOwner(), reply.getContent(), reply.getId(), true);
        } else if (item.getItemId() == R.id.ctx_menu_reply_delete) {
            viewModel.deleteReply(reply.getId());
        } else {
            replyPopUp(reply.getOwner(), reply.getContent(), reply.getId(), false);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void replyPopUp(String owner, String content, String quoteID, boolean editing) {

        View popupView = getLayoutInflater().inflate(R.layout.popup_reply, new LinearLayout(this));

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // If you need the PopupWindow to dismiss when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popup_window_animation_slide);

        // Adjust popup window location when keyboard pops up
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        TextInputLayout textField = popupWindow.getContentView().findViewById(R.id.popup);

        textField.setEndIconVisible(false);
        if (editing)
            Objects.requireNonNull(textField.getEditText()).setText(content);
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
                if (editing)
                    viewModel.editReply(quoteID, textField.getEditText().getText().toString());
                else
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
