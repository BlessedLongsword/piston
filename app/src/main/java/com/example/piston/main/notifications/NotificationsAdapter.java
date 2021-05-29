package com.example.piston.main.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
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
import com.example.piston.data.notifications.Notification;
import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.notifications.NotificationReply;
import com.example.piston.databinding.ItemNotificationPostBinding;
import com.example.piston.databinding.ItemNotificationReplyBinding;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.utilities.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final FragmentActivity localActivity;
    private final NotificationsViewModel viewModel;
    private OnItemClick itemClick;
    private final SparseBooleanArray selectedItems;
    private int selectedIndex = -1;
    final boolean darkModeEnabled;
    private ColorStateList color;

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public static class NotificationPostHolder extends RecyclerView.ViewHolder {

        private final ItemNotificationPostBinding binding;

        public NotificationPostHolder(ItemNotificationPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NotificationPost item) {
            binding.setNotification(item);
        }

        public ItemNotificationPostBinding getBinding() {
            return binding;
        }

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

    public NotificationsAdapter(FragmentActivity activity, boolean darkModeEnabled){
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(NotificationsViewModel.class);
        viewModel.getNotifications().observe(activity, notifications -> notifyDataSetChanged());
        selectedItems = new SparseBooleanArray();
        this.darkModeEnabled = darkModeEnabled;
    }

    @Override
    public int getItemViewType(int position) {
        return getNotificationType(Objects.requireNonNull(viewModel.getNotifications().getValue()).get(position));
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0 || viewType == 2) {
            ItemNotificationPostBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_notification_post, parent, false);
            color = binding.notificationPostCard.getCardBackgroundColor();
            if (viewType == 2){
                binding.notificationTitle.setTextColor(ContextCompat.getColor(localActivity,
                        (darkModeEnabled) ? R.color.disabled_dark : R.color.disabled));
            }

            return new NotificationsAdapter.NotificationPostHolder(binding);
        }
        else {
            ItemNotificationReplyBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.item_notification_reply, parent, false);
            color = binding.notificationReplyCard.getCardBackgroundColor();
            if (viewType == 3){
                binding.notificationTitle.setTextColor(ContextCompat.getColor(localActivity,
                        (darkModeEnabled) ? R.color.disabled_dark : R.color.disabled));
            }
            return new NotificationsAdapter.NotificationReplyHolder(binding);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0 || holder.getItemViewType() == 2) {
            NotificationPost notificationPost = Objects.requireNonNull((NotificationPost) Objects.requireNonNull(viewModel
                    .getNotifications().getValue()).get(position));
            NotificationsAdapter.NotificationPostHolder hold = (NotificationsAdapter
                    .NotificationPostHolder) holder;
            hold.bind(notificationPost);

            Glide.with(localActivity)
                    .load(notificationPost.getContextImageLink())
                    .placeholder(R.drawable.default_profile_picture)
                    .into(hold.binding.notificationProfilePicture);

            Glide.with(localActivity)
                    .load(notificationPost.getImageLink())
                    .into(hold.binding.notificationPostPicture);

            hold.getBinding().notificationPostCard.setActivated(selectedItems.get(position,false));

            if(selectedItemCount()==0){
                hold.getBinding().notificationPostCard.setOnClickListener(openNewActivity(
                        notificationPost.getScope(), notificationPost.getSectionID(),
                        notificationPost.getPostID(), null));
            }
            else{
                hold.getBinding().notificationPostCard.setOnClickListener(view -> itemClick
                        .onLongPress(view, viewModel.getNotifications().getValue().get(position), position));
                hold.getBinding().notificationPostCard.setOnClickListener(view ->
                        itemClick.onLongPress(view, viewModel.getNotifications()
                                .getValue().get(position), position));
            }
            hold.getBinding().notificationPostCard.setOnLongClickListener(view -> {
                if (itemClick == null) {
                    return false;
                } else {
                    itemClick.onLongPress(view, viewModel.getNotifications().getValue().get(position), position);
                    notifyDataSetChanged();
                    return true;
                }                });
            hold.getBinding().notificationPostCard.setOnLongClickListener(view -> {
                if (itemClick == null) {
                    return false;
                } else {
                    itemClick.onLongPress(view, viewModel.getNotifications()
                            .getValue().get(position), position);
                    notifyDataSetChanged();
                    return true;
                }                });
            if (selectedItems.get(position, false)) {
                hold.getBinding().postProfile.setVisibility(View.GONE);
                hold.getBinding().postCheck.setVisibility(View.VISIBLE);
                if (selectedIndex == position) selectedIndex = -1;
                hold.getBinding().notificationPostCard.setCardBackgroundColor(ContextCompat
                        .getColor(localActivity, R.color.selected));
            } else {
                hold.getBinding().postProfile.setVisibility(View.VISIBLE);
                hold.getBinding().postCheck.setVisibility(View.GONE);
                if (selectedIndex == position) selectedIndex = -1;
                hold.getBinding().notificationPostCard.setCardBackgroundColor(color);
            }
        }
        else {
            NotificationReply notificationReply = Objects.requireNonNull((NotificationReply) Objects.requireNonNull(viewModel
                    .getNotifications().getValue()).get(position));
            NotificationsAdapter.NotificationReplyHolder hold = (NotificationsAdapter
                    .NotificationReplyHolder) holder;
            hold.bind(notificationReply);

            Glide.with(localActivity)
                    .load(notificationReply.getContextImageLink())
                    .placeholder(R.drawable.default_profile_picture)
                    .into(hold.binding.notificationProfilePicture);

            hold.getBinding().notificationReplyCard.setActivated(selectedItems.get(position,false));

            if (selectedItemCount()==0){
                hold.getBinding().notificationReplyCard.setOnClickListener(openNewActivity(
                        notificationReply.getScope(), notificationReply.getSectionID(),
                        notificationReply.getPostID(), notificationReply.getReplyID()));
            }
            else{
                hold.getBinding().notificationReplyCard.setOnClickListener(view ->
                        itemClick.onLongPress(view, viewModel.getNotifications()
                                .getValue().get(position), position));
                hold.getBinding().notificationReplyCard.setOnClickListener(view -> itemClick
                        .onLongPress(view, viewModel.getNotifications().getValue().get(position), position));
            }
            hold.getBinding().notificationReplyCard.setOnLongClickListener(view -> {
                if (itemClick == null) {
                    return false;
                } else {
                    itemClick.onLongPress(view, viewModel.getNotifications()
                            .getValue().get(position), position);
                    notifyDataSetChanged();
                    return true;
                }                });
            hold.getBinding().notificationReplyCard.setOnLongClickListener(view -> {
                if (itemClick == null) {
                    return false;
                } else {
                    itemClick.onLongPress(view, viewModel.getNotifications().getValue().get(position), position);
                    notifyDataSetChanged();
                    return true;
                }                });
            if (selectedItems.get(position, false)) {
                hold.getBinding().replyProfile.setVisibility(View.GONE);
                hold.getBinding().replyCheck.setVisibility(View.VISIBLE);
                if (selectedIndex == position) selectedIndex = -1;
                hold.getBinding().notificationReplyCard.setCardBackgroundColor(ContextCompat
                        .getColor(localActivity, R.color.selected));
            } else {
                hold.getBinding().replyProfile.setVisibility(View.VISIBLE);
                hold.getBinding().replyCheck.setVisibility(View.GONE);
                if (selectedIndex == position) selectedIndex = -1;
                hold.getBinding().notificationReplyCard.setCardBackgroundColor(color);
            }
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == 0 || holder.getItemViewType() == 2) {
            ((NotificationPostHolder) holder)
                    .getBinding().notificationPostCard.setOnClickListener(null);
        }
        else {
            ((NotificationReplyHolder) holder)
                    .getBinding().notificationReplyCard.setOnClickListener(null);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getNotifications().getValue()).size();
    }

    public int getNotificationType(Notification notification) {
        if (notification instanceof NotificationPost) {
            if (notification.getRead()){
                return 2;
            }
            else{
                return 0;
            }
        } else {
            if (notification.getRead()){
                return 3;
            }
            else{
                return 1;
            }
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

    public interface OnItemClick {
        void onLongPress(View view, Notification notification, int position);
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectedIndex = position;
        if (selectedItems.get(position, false))
            selectedItems.delete(position);
        else
            selectedItems.put(position, true);
        notifyItemChanged(position);
    }

    public int selectedItemCount() {
        return selectedItems.size();
    }

    public void deleteNotifications() {
        for (int i = getSelectedItems().size() - 1; i >= 0; i--) {
            viewModel.deleteNotification(Objects.requireNonNull(viewModel.getNotifications()
                    .getValue()).get(getSelectedItems().get(i)).getNotificationID());
        }
        viewModel.updateNotifications();
    }

    public void markAsRead() {
        boolean changed = false;
        for(int i = getSelectedItems().size() - 1; i >= 0; i--) {
            Notification notification = Objects.requireNonNull(viewModel.getNotifications()
                    .getValue()).get(getSelectedItems().get(i));
            if (!notification.getRead()) {
                changed = true;
                viewModel.markAsRead(notification.getNotificationID());
            }
        }
        if (changed)
            viewModel.updateNotifications();
    }

}
