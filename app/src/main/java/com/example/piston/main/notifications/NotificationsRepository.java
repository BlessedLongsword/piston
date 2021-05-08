package com.example.piston.main.notifications;

import com.example.piston.data.Notification;
import com.example.piston.data.NotificationPost;
import com.example.piston.data.NotificationReply;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationsRepository {

    private final INotifications listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference docRef;
    private ListenerRegistration listenerRegistration;


    public interface INotifications {
        void setNotifications(ArrayList<Notification> notifications);
    }

    public NotificationsRepository(INotifications listener) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        docRef = db.collection("users")
                .document(email);
        listenChanges();
    }

    public void loadNotifications() {
        ArrayList<Notification> notifications = new ArrayList<>();
        docRef.collection("notifications")
                .orderBy("timestamp")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : Objects.
                                requireNonNull(task.getResult())) {
                            String notificationType = Objects.requireNonNull(documentSnapshot
                                    .get("type")).toString();
                            if (notificationType.equals("post")) {
                                NotificationPost notification = documentSnapshot
                                        .toObject(NotificationPost.class);
                                notifications.add(notification);
                            } else {
                                NotificationReply notification = documentSnapshot
                                        .toObject(NotificationReply.class);
                                notifications.add(notification);
                            }
                        }
                        listener.setNotifications(notifications);
                    }
        });
    }

    private void listenChanges() {
        listenerRegistration = docRef.collection("notifications")
                .addSnapshotListener((snapshots, e) -> NotificationsRepository.this.loadNotifications());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
