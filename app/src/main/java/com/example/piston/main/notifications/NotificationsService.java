package com.example.piston.main.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.piston.R;
import com.example.piston.data.notifications.Notification;
import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.notifications.NotificationReply;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.main.settings.SettingsActivity;
import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.io.IOException;
import java.util.Objects;

public class NotificationsService extends Service {

    private boolean first = true;
    private ListenerRegistration listener;
    private Bitmap sectionImage;

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
                    .setSmallIcon(R.mipmap.logo)
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

                    Object type = documentSnapshot.get("type");

                    if (type != null) {
                        String notificationType = type.toString();
                        if (notificationType.equals("post")) {
                            NotificationPost notification = documentSnapshot
                                    .toObject(NotificationPost.class);
                            Glide.with(this).asBitmap()
                                    .load(notification.getContextImageLink())
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            postNotificationPost(notification, resource, getCircleBitmap(sectionImage));
                                        }
                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        } else {
                            NotificationReply notification = documentSnapshot
                                    .toObject(NotificationReply.class);
                            Glide.with(this).asBitmap()
                                    .load(notification.getContextImageLink())
                                    .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    postNotificationReply(notification, getCircleBitmap(resource));
                                }
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                        }
                    }
                });
    }

    public void postNotificationPost(NotificationPost notification, Bitmap postImage, Bitmap sectionImage) {

        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra(Values.SCOPE, notification.getScope());
        intent.putExtra(Values.SECTION_ID, notification.getSectionID());
        intent.putExtra(Values.POST_ID, notification.getPostID());
        intent.putExtra(Values.ORPHAN, true);

        PendingIntent contentIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.outline_post_add_black_24)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getSectionName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(sectionImage)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(postImage)
                        .bigLargeIcon(sectionImage))
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
        intent.putExtra(Values.ORPHAN, true);

        PendingIntent contentIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
