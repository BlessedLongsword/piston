package com.example.piston.main.posts;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.data.posts.QuoteReply;
import com.example.piston.data.posts.Reply;
import com.example.piston.databinding.ItemQuoteReplyBinding;
import com.example.piston.databinding.ItemReplyBinding;
import com.example.piston.main.profile.ProfileActivity;
import com.example.piston.utilities.Values;

import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final PostViewModel viewModel;
    private final PostAdapterListener listener;
    private final FragmentActivity localActivity;
    private int position;

    public interface PostAdapterListener {
        void quoteOnClick(String quoteID);
        void replyPopUp(String owner, String content, String quoteID, boolean editing);
    }

    public static class ReplyHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        private final ItemReplyBinding binding;
        private final String currentUser;

        public ReplyHolder(ItemReplyBinding binding, String currentUser) {
            super(binding.getRoot());
            this.binding = binding;
            this.currentUser = currentUser;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        public void bind(Reply item) {
            binding.setReply(item);
        }

        public ItemReplyBinding getBinding() {
            return binding;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (currentUser.equals(binding.getReply().getOwner())) {
                menu.add(Menu.NONE, R.id.ctx_menu_reply_edit, Menu.NONE, R.string.edit);
                menu.add(Menu.NONE, R.id.ctx_menu_reply_delete, Menu.NONE, R.string.delete);
            }
            menu.add(Menu.NONE, R.id.ctx_menu_reply_reply, Menu.NONE, R.string.reply); }
    }

    public static class QuoteReplyHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        private final ItemQuoteReplyBinding binding;
        private final String currentUser;

        public QuoteReplyHolder(ItemQuoteReplyBinding binding, String currentUser) {
            super(binding.getRoot());
            this.binding = binding;
            this.currentUser = currentUser;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        public void bind(QuoteReply item) {
            binding.setReply(item);
        }

        public ItemQuoteReplyBinding getBinding() {
            return binding;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (currentUser.equals(binding.getReply().getOwner())) {
                menu.add(Menu.NONE, R.id.ctx_menu_reply_edit, Menu.NONE, R.string.edit);
                menu.add(Menu.NONE, R.id.ctx_menu_reply_delete, Menu.NONE, R.string.delete);
            }
            menu.add(Menu.NONE, R.id.ctx_menu_reply_reply, Menu.NONE, R.string.reply);
        }
    }

    public PostAdapter(FragmentActivity activity, PostAdapterListener listener) {
        this.listener = listener;
        this.localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(PostViewModel.class);
        viewModel.getReplies().observe(activity, replies -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        return getReplyType(Objects.requireNonNull(viewModel.getReplies().getValue()).get(position));
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            ItemReplyBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_reply, parent, false);
            ReplyHolder holder = new ReplyHolder(binding, viewModel.getCurrentUser().getValue());
            binding.replyButton.setOnClickListener(v -> listener.replyPopUp(binding.replyOwner.getText().toString(),
                    binding.replyContent.getText().toString(), binding.getReply().getId(), false));
            return holder;
        }
        ItemQuoteReplyBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_quote_reply, parent, false);
        QuoteReplyHolder holder = new QuoteReplyHolder(binding, viewModel.getCurrentUser().getValue());
        binding.replyButton.setOnClickListener(v -> listener.replyPopUp(binding.replyOwner.getText().toString(),
                binding.replyContent.getText().toString(), binding.getReply().getId(), false));
        binding.replyQuote.setOnClickListener(v ->
                listener.quoteOnClick(holder.binding.getReply().getQuoteID()));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            Reply reply = Objects.requireNonNull(viewModel.getReplies().getValue()).get(position);
            ReplyHolder hold = ((ReplyHolder) holder);
            hold.itemView.setTag(reply.getId());
            hold.bind(reply);

            Glide.with(localActivity)
                    .load(reply.getOwnerImageLink())
                    .placeholder(R.drawable.default_profile_picture)
                    .into(hold.binding.replyProfilePicture);

            if (viewModel.getScope().equals(Values.PERSONAL)) {
                hold.getBinding().userProfile.setVisibility(View.GONE);
                hold.getBinding().replyButton.setText(R.string.add_note);
                hold.getBinding().replyButton.setIcon(ContextCompat.getDrawable(localActivity,
                        R.drawable.outline_post_add_black_24));
            }

            ((ReplyHolder) holder).getBinding().userProfile.setOnClickListener(openNewActivity
                    (reply.getOwnerEmail()));
            ((ReplyHolder) holder).getBinding().card.setOnLongClickListener(v -> {
                setPosition(position);
                return false;
            });
        } else {
            QuoteReply reply = (QuoteReply) Objects.requireNonNull(viewModel.getReplies().getValue()).get(position);
            QuoteReplyHolder hold = ((QuoteReplyHolder) holder);
            hold.itemView.setTag(reply.getId());
            hold.bind(reply);

            Glide.with(localActivity)
                    .load(reply.getOwnerImageLink())
                    .placeholder(R.drawable.default_profile_picture)
                    .into(hold.binding.replyQuoteProfilePicture);

            if (viewModel.getScope().equals(Values.PERSONAL)) {
                hold.getBinding().userProfile.setVisibility(View.GONE);
                hold.getBinding().replyButton.setText(R.string.add_note);
                hold.getBinding().quoteOwnerLayout.setVisibility(View.GONE);
                hold.getBinding().replyButton.setIcon(ContextCompat.getDrawable(localActivity,
                        R.drawable.outline_post_add_black_24));
            }
            ((QuoteReplyHolder) holder).getBinding().userProfile.setOnClickListener(openNewActivity
                    (reply.getOwnerEmail()));
            ((QuoteReplyHolder) holder).getBinding().card.setOnLongClickListener(v -> {
                setPosition(position);
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getReplies().getValue()).size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == 1)
            ((ReplyHolder) holder).getBinding().card.setOnLongClickListener(null);
        else
            ((QuoteReplyHolder) holder).getBinding().card.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public int getReplyType(Reply reply) {
        if (reply instanceof QuoteReply) {
            return 2;
        } else {
            return 1;
        }
    }

    private View.OnClickListener openNewActivity(String email) {
        return v -> {
            Intent intent = new Intent(localActivity, ProfileActivity.class);
            intent.putExtra(Values.EMAIL, email);
            localActivity.startActivity(intent);
        };
    }

}

