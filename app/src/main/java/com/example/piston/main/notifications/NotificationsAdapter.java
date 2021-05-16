package com.example.piston.main.notifications;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.data.notifications.Notification;
import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.notifications.NotificationReply;
import com.example.piston.databinding.ItemNotificationPostBinding;
import com.example.piston.databinding.ItemNotificationReplyBinding;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.utilities.Values;

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

        public void bind(NotificationPost item) {
            binding.setNotification(item);
        }

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
        viewModel.getNotifications().observe(activity, notifications -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        return getNotificationType(Objects.requireNonNull(viewModel.getNotifications().getValue()).get(position));
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
            NotificationPost notificationPost = Objects.requireNonNull((NotificationPost) Objects.requireNonNull(viewModel
                    .getNotifications().getValue()).get(position));
            NotificationsAdapter.NotificationPostHolder hold = (NotificationsAdapter
                    .NotificationPostHolder) holder;
            hold.bind(notificationPost);
            if (notificationPost.getUserImageLink() != null) {
                if (!notificationPost.getUserImageLink().equals("")) {
                    Glide.with(localActivity)
                            .load(notificationPost.getUserImageLink())
                            .into(hold.binding.notificationProfilePicture);
                }
            }
            Glide.with(localActivity)
                    .load(notificationPost.getImageLink())
                    .into(hold.binding.notificationPostPicture);
            hold.getBinding().notificationPostCard.setOnClickListener(openNewActivity(
                    notificationPost.getScope(), notificationPost.getSectionID(),
                            notificationPost.getPostID(), null));
        }
        else {
            NotificationReply notificationReply = Objects.requireNonNull((NotificationReply) Objects.requireNonNull(viewModel
                    .getNotifications().getValue()).get(position));
            NotificationsAdapter.NotificationReplyHolder hold = (NotificationsAdapter
                    .NotificationReplyHolder) holder;
            hold.bind(notificationReply);
            if (notificationReply.getUserImageLink() != null) {
                if (!notificationReply.getUserImageLink().equals("")) {
                    Glide.with(localActivity)
                            .load(notificationReply.getUserImageLink())
                            .into(hold.binding.notificationProfilePicture);
                }
            }
            hold.getBinding().notificationReplyCard.setOnClickListener(openNewActivity(
                    notificationReply.getScope(), notificationReply.getSectionID(),
                            notificationReply.getPostID(), notificationReply.getReplyID()));
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

    private View.OnClickListener openNewActivity(String scope, String sectionID, String postID, String replyID) {
        return v -> {
            Intent intent = new Intent(localActivity, PostActivity.class);
            intent.putExtra(Values.SCOPE, scope);
            intent.putExtra(Values.SECTION_ID, sectionID);
            intent.putExtra(Values.POST_ID, postID);
            intent.putExtra(Values.REPLY_ID, replyID);
            intent.putExtra(Values.ORPHAN, true);
            localActivity.startActivity(intent);
            localActivity.finish();
        };
    }

}
