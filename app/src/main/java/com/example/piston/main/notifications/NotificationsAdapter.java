package com.example.piston.main.notifications;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.data.Notification;
import com.example.piston.data.NotificationPost;
import com.example.piston.data.NotificationReply;
import com.example.piston.databinding.ItemNotificationPostBinding;
import com.example.piston.databinding.ItemNotificationReplyBinding;
import com.example.piston.main.global.category.CategoryActivity;

import java.util.Objects;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final FragmentActivity localActivity;
    private final NotificationsViewModel viewModel;

    public static class NotificationPostHolder extends RecyclerView.ViewHolder {

        private final ItemNotificationPostBinding binding;

        public NotificationPostHolder(ItemNotificationPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NotificationPost item) { binding.setNotification(item); }

        public ItemNotificationPostBinding getBinding() { return binding; }

    }

    public static class NotificationReplyHolder extends RecyclerView.ViewHolder {

        private final ItemNotificationReplyBinding binding;

        public NotificationReplyHolder(ItemNotificationReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NotificationReply item) { binding.setNotification(item); }

        public ItemNotificationReplyBinding getBinding() { return binding; }

    }

    public NotificationsAdapter(FragmentActivity activity){
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(NotificationsViewModel.class);
        viewModel.getNotifications().observe(activity, cosa -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        return getNotificationType(viewModel.getNotifications().getValue().get(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            ItemNotificationPostBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_notification_post, parent, false);
            return new NotificationsAdapter.NotificationPostHolder(binding);
        }
        else {
            ItemNotificationReplyBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_notification_reply, parent, false);
            return new NotificationsAdapter.NotificationReplyHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            NotificationPost notificationPost = Objects.requireNonNull((NotificationPost) viewModel
                    .getNotifications().getValue().get(position));
            NotificationsAdapter.NotificationPostHolder hold = (NotificationsAdapter
                    .NotificationPostHolder) holder;
            hold.bind(notificationPost);
            Glide.with(localActivity)
                    .load(notificationPost.getImageLink())
                    .into(hold.binding.notificationPostPicture);
        }
        else {
            NotificationReply notificationReply = Objects.requireNonNull((NotificationReply) viewModel
                    .getNotifications().getValue().get(position));
            NotificationsAdapter.NotificationReplyHolder hold = (NotificationsAdapter
                    .NotificationReplyHolder) holder;
            hold.bind(notificationReply);
        }
    }

    @Override
    public int getItemCount() {
        if (viewModel.getNotifications().getValue() == null) {
            return 0;
        }
        return Objects.requireNonNull(viewModel.getNotifications().getValue()).size();
    }

    public int getNotificationType(Notification notification) {
        if (notification instanceof NotificationPost) {
            return 0;
        } else {
            return 1;
        }
    }

}
