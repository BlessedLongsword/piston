package com.example.piston.main.notifications;

import com.example.piston.data.notifications.Notification;
import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.notifications.NotificationReply;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationsRepository {

    private final INotifications listener;
    private final DocumentReference userDocRef;
    private ListenerRegistration listenerRegistration;
    private final String email;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public interface INotifications {
        void setNotifications(ArrayList<Notification> notifications);
    }

    public NotificationsRepository(INotifications listener) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();

        userDocRef = db.collection("users").document(Objects.requireNonNull(email));

        listenChanges();
    }

    public void loadNotifications() {
        ArrayList<Notification> notifications = new ArrayList<>();
        userDocRef.collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int counter = 0;
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
                            if (counter++ % 10 == 0)
                                    listener.setNotifications(notifications);
                        }
                        listener.setNotifications(notifications);
                    }
        });
    }

    private void listenChanges() {
        listenerRegistration = userDocRef.collection("notifications")
                .addSnapshotListener((snapshots, error) -> {
                    if (Objects.requireNonNull(snapshots).getDocumentChanges().get(0).getType().equals(DocumentChange.Type.ADDED))
                        NotificationsRepository.this.loadNotifications();
                });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

    public void deleteNotification(String id) {
        db.collection("users").document(email).collection("notifications").document(id).delete();
    }
    public void markAsRead(String id){
        db.collection("users").document(email).collection("notifications").document(id).update("read",true);
    }
}
