package com.example.piston.main.notifications;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.main.global.category.CategoryActivity;

import java.util.Objects;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder> {

    private final FragmentActivity localActivity;
    private final NotificationsViewModel viewModel;

    public static class NotificationHolder extends RecyclerView.ViewHolder {

        private final FrameLayout layout;
        private final TextView notificationTitle, notificationText;

        public NotificationHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.notification_card);
            notificationTitle = view.findViewById(R.id.notification_title);
            notificationText = view.findViewById(R.id.notification_text);
        }

        public TextView getNotificationTitle() {
            return notificationTitle;
        }

        public TextView getNotificationText() {
            return notificationText;
        }

        public FrameLayout getLayout() {
            return layout;
        }
    }

    public NotificationsAdapter(FragmentActivity activity){
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(NotificationsViewModel.class);
        viewModel.getNotifications().observe(activity, cosa -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public NotificationsAdapter.NotificationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_notification, viewGroup, false);
        return new NotificationsAdapter.NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.NotificationHolder notificationHolder, int position) {
        notificationHolder.getNotificationTitle().setText(Objects.requireNonNull(viewModel.getNotifications().getValue()).get(position).getTitle());
        notificationHolder.getNotificationText().setText(viewModel.getNotifications().getValue().get(position).getDescription());
        notificationHolder.getLayout().setOnClickListener(view -> {
            Intent intent = new Intent(localActivity, CategoryActivity.class);
            intent.putExtra("title", viewModel.getNotifications().getValue().get(position).getTitle());
            intent.putExtra("description", viewModel.getNotifications().getValue().get(position).getDescription());
            localActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(viewModel.getNotifications().getValue()).size();
    }

}
