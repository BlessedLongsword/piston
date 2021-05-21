package com.example.piston.main.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.piston.R;
import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.notifications.NotificationReply;
import com.example.piston.main.settings.SettingsActivity;
import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
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
                            postNotification(notification.getTitle(), notification.getSectionID(),
                                    documentSnapshot.getId());
                        } else {
                            NotificationReply notification = documentSnapshot
                                    .toObject(NotificationReply.class);
                            postNotification(notification.getUser(), notification.getContent(),
                                    documentSnapshot.getId());
                        }
                    }
                });
    }

    public void postNotification(String title, String content, String id) {

        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this,
                        NotificationsActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.id.group_moderator_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id.hashCode(), builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener.remove();
    }
}
