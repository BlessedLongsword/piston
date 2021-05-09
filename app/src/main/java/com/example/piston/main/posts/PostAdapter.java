package com.example.piston.main.posts;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.data.Post;
import com.example.piston.data.Reply;
import com.example.piston.databinding.ItemReplyBinding;
import com.example.piston.databinding.ItemThreadBinding;
import com.example.piston.utilities.textwatchers.BaseTextWatcher;
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final FragmentActivity localActivity;
    private final PostViewModel viewModel;
    private final PostAdapterListener listener;

    public static class ThreadHolder extends RecyclerView.ViewHolder {

        private final ItemThreadBinding binding;

        public ThreadHolder(ItemThreadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Post item) {
            binding.setThread(item);
        }

        public ItemThreadBinding getBinding() {
            return binding;
        }

    }

    public static class ReplyHolder extends RecyclerView.ViewHolder {

        private final ItemReplyBinding binding;

        public ReplyHolder(ItemReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Reply item) {
            binding.setReply(item);
        }

        public ItemReplyBinding getBinding() {
            return binding;
        }
    }

    public PostAdapter(FragmentActivity activity, PostAdapterListener listener) {
        localActivity = activity;
        this.listener = listener;
        viewModel = new ViewModelProvider(activity).get(PostViewModel.class);
        viewModel.getPost().observe(activity, cosa -> notifyDataSetChanged());
        viewModel.getReplies().observe(activity, cosa -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0){
            ItemThreadBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_thread, parent, false);
            binding.threadReplyButton.setOnClickListener(v -> replyPopUp(null, null, null));
            return new PostAdapter.ThreadHolder(binding);
        }
        else {
            ItemReplyBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_reply, parent, false);
            PostAdapter.ReplyHolder holder = new PostAdapter.ReplyHolder(binding);
            binding.replyButton.setOnClickListener(v -> replyPopUp(binding.replyOwner.getText().toString(),
                    binding.replyContent.getText().toString(), binding.getReply().getId()));
            binding.replyQuote.setOnClickListener(v -> {
                listener.quoteOnClick(v, holder.binding.getReply().getQuoteID());
            });
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == 0) {
            Post post = Objects.requireNonNull(viewModel.getPost().getValue());
            PostAdapter.ThreadHolder hold  = (PostAdapter.ThreadHolder) holder;
            hold.bind(post);
            Glide.with(localActivity)
                    .load(post.getImageLink())
                    .into(hold.binding.postPicture);
        }
        else {
            Reply reply = Objects.requireNonNull(viewModel.getReplies().getValue()).get(position-1);
            PostAdapter.ReplyHolder hold = ((PostAdapter.ReplyHolder) holder);
            hold.bind(reply);
            if (reply.getQuote() != null)
                hold.binding.replyQuote.setVisibility(View.VISIBLE);
        }

    }

    public interface PostAdapterListener {
        void quoteOnClick(View v, String quoteID);
    }

    @Override
    public int getItemCount() {
        if (viewModel.getPost().getValue() == null)
            return 0;
        return Objects.requireNonNull(viewModel.getReplies().getValue()).size() + 1;
    }

    public void replyPopUp(String owner, String content, String quoteID) {
        View popupView = localActivity.getLayoutInflater().inflate(R.layout.pupup_reply, null);
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
        InputMethodManager imm = (InputMethodManager) localActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        // dims background when popup window shows up
        WindowManager.LayoutParams lp = localActivity.getWindow().getAttributes();
        lp.alpha=0.5f;
        localActivity.getWindow().setAttributes(lp);

        // restore dim
        popupWindow.setOnDismissListener(() -> {
            lp.alpha=1f;
            localActivity.getWindow().setAttributes(lp);
        });
    }

}

