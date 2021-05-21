package com.example.piston.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.piston.R;
import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.notifications.NotificationReply;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class NotificationsService extends Service {

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

    private int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        DocumentReference userDocRef = db.collection("users").document(Objects.requireNonNull(email));
        userDocRef.collection("notifications")
                .addSnapshotListener((snapshots, error) -> {

                    DocumentSnapshot documentSnapshot = Objects.requireNonNull(snapshots).
                            getDocumentChanges().get(0).getDocument();

                    Object type = documentSnapshot.get("type");

                    if (type != null) {
                        String notificationType = type.toString();
                        if (notificationType.equals("post")) {
                            NotificationPost notification = documentSnapshot
                                    .toObject(NotificationPost.class);
                            postNotification(notification.getTitle(), notification.getSectionID());
                        } else {
                            NotificationReply notification = documentSnapshot
                                    .toObject(NotificationReply.class);
                            postNotification(notification.getUser(), notification.getContent());
                        }
                    }
                });
    }

    public void postNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.id.group_moderator_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(counter++, builder.build());
    }
}
