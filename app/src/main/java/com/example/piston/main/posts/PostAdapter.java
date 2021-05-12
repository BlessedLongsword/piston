package com.example.piston.main.posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.data.QuoteReply;
import com.example.piston.data.Reply;
import com.example.piston.databinding.ItemQuoteReplyBinding;
import com.example.piston.databinding.ItemReplyBinding;

import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final PostViewModel viewModel;
    private final PostAdapterListener listener;

    public interface PostAdapterListener {
        void quoteOnClick(View v, String quoteID);
        void replyPopUp(String owner, String content, String quoteID);
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

    public static class QuoteReplyHolder extends RecyclerView.ViewHolder {
        private final ItemQuoteReplyBinding binding;

        public QuoteReplyHolder(ItemQuoteReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(QuoteReply item) {
            binding.setReply(item);
        }

        public ItemQuoteReplyBinding getBinding() {
            return binding;
        }
    }

    public PostAdapter(FragmentActivity activity, PostAdapterListener listener) {
        this.listener = listener;
        viewModel = new ViewModelProvider(activity).get(PostViewModel.class);
        viewModel.getReplies().observe(activity, replies -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        return getReplyType(Objects.requireNonNull(viewModel.getReplies().getValue()).get(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            ItemReplyBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_reply, parent, false);
            ReplyHolder holder = new ReplyHolder(binding);
            binding.replyButton.setOnClickListener(v -> listener.replyPopUp(binding.replyOwner.getText().toString(),
                    binding.replyContent.getText().toString(), binding.getReply().getId()));
            return holder;
        }
        ItemQuoteReplyBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_quote_reply, parent, false);
        QuoteReplyHolder holder = new QuoteReplyHolder(binding);
        binding.replyButton.setOnClickListener(v -> listener.replyPopUp(binding.replyOwner.getText().toString(),
                binding.replyContent.getText().toString(), binding.getReply().getId()));
        binding.replyQuote.setOnClickListener(v ->
                listener.quoteOnClick(v, holder.binding.getReply().getQuoteID()));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            Reply reply = Objects.requireNonNull(viewModel.getReplies().getValue()).get(position);
            ReplyHolder hold = ((ReplyHolder) holder);
            hold.itemView.setTag(reply.getId());
            hold.bind(reply);
        } else {
            QuoteReply reply = (QuoteReply) Objects.requireNonNull(viewModel.getReplies().getValue()).get(position);
            QuoteReplyHolder hold = ((QuoteReplyHolder) holder);
            hold.itemView.setTag(reply.getId());
            hold.bind(reply);
        }
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getReplies().getValue()).size();
    }

    public int getReplyType(Reply reply) {
        if (reply instanceof QuoteReply) {
            return 2;
        } else {
            return 1;
        }
    }

}

