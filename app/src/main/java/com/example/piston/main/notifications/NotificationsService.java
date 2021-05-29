package com.example.piston.main.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.piston.R;
import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.notifications.NotificationReply;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.main.settings.SettingsActivity;
import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

public class NotificationsService extends Service {

    private boolean first = true;
    private ListenerRegistration listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final String CHANNEL_ID = "PISTON_NOTIFICATION";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel name";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(Values.NOTIFICATIONS_END, true);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle("Piston is running")
                    .setContentText("Tap to stop listening for notifications")
                    .setGroup("pistonAlone")
                    .setContentIntent(contentIntent);

            startForeground(1, builder.build());
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        DocumentReference userDocRef = db.collection("users").document(Objects.requireNonNull(email));
        listener = userDocRef.collection("notifications")
                .addSnapshotListener((snapshots, error) -> {

                    if (first) {
                        first = false;
                        return;
                    }

                    DocumentSnapshot documentSnapshot = Objects.requireNonNull(snapshots).
                            getDocumentChanges().get(0).getDocument();

                    if (snapshots.getDocumentChanges().get(0).getType().equals(DocumentChange.Type.REMOVED))
                        return;

                    Object type = documentSnapshot.get("type");

                    if (type != null) {
                        String notificationType = type.toString();
                        if (notificationType.equals("post")) {
                            NotificationPost notification = documentSnapshot
                                    .toObject(NotificationPost.class);
                            if (notification.getRead())
                                return;
                            if (notification.getImageLink() != null)
                                Glide.with(this).asBitmap()
                                        .load(notification.getImageLink())
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                postNotificationPost(notification, resource);
                                            }
                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });
                            else {
                                Glide.with(this).asBitmap()
                                        .load(notification.getContextImageLink())
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                postNotificationPostNoImage(notification, resource);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });
                            }
                        } else {
                            NotificationReply notification = documentSnapshot
                                    .toObject(NotificationReply.class);
                            if (notification.getRead())
                                return;
                            Log.d("DBReadTAG", "got here: " + notification.getContextImageLink());
                            if (notification.getContextImageLink() != null) {
                                Glide.with(this).asBitmap()
                                        .load(notification.getContextImageLink())
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                postNotificationReply(notification, resource);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });
                            } else {
                                Bitmap userImage = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.default_profile_picture);
                                postNotificationReply(notification, userImage);
                            }
                        }
                    }
                });
    }

    public void postNotificationPost(NotificationPost notification, Bitmap postImage) {

        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra(Values.SCOPE, notification.getScope());
        intent.putExtra(Values.SECTION_ID, notification.getSectionID());
        intent.putExtra(Values.POST_ID, notification.getPostID());
        intent.putExtra(Values.FROM_NOTIFICATION, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.outline_post_add_black_24)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getSectionName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(postImage)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(postImage)
                        .bigLargeIcon(null))
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notification.getNotificationID().hashCode(), builder.build());
    }

    public void postNotificationPostNoImage(NotificationPost notification, Bitmap image) {

        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra(Values.SCOPE, notification.getScope());
        intent.putExtra(Values.SECTION_ID, notification.getSectionID());
        intent.putExtra(Values.POST_ID, notification.getPostID());
        intent.putExtra(Values.FROM_NOTIFICATION, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.outline_post_add_black_24)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getSectionName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(image)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notification.getNotificationID().hashCode(), builder.build());
    }

    public void postNotificationReply(NotificationReply notification, Bitmap userImage) {

        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra(Values.SCOPE, notification.getScope());
        intent.putExtra(Values.SECTION_ID, notification.getSectionID());
        intent.putExtra(Values.POST_ID, notification.getPostID());
        intent.putExtra(Values.FROM_NOTIFICATION, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.outline_reply_white_24)
                .setContentTitle(notification.getUser() + " " + getString(R.string.replied))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(userImage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notification.getContent()))
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notification.getNotificationID().hashCode(), builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener.remove();
    }
}
