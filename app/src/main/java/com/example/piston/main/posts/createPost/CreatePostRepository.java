package com.example.piston.main.posts.createPost;

import android.util.Log;

import com.example.piston.data.Notification;
import com.example.piston.data.NotificationPost;
import com.example.piston.data.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CreatePostRepository {
    private final CreatePostRepository.ICreatePost listener;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    final String user;
    String username;

    public interface ICreatePost {
        void setTitleStatus(CreatePostResult.TitleError titleError);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
        void setErrorMessage(String message);
    }

    public CreatePostRepository(CreatePostRepository.ICreatePost listener) {
        this.listener = listener;
        user = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        db.collection("users")
                .document(Objects.requireNonNull(user))
                .get()
                .addOnCompleteListener(task ->
                        username = (String) Objects.requireNonNull(task.getResult()).get("username"));
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreatePostResult.TitleError.EMPTY);
        else {
            listener.setTitleStatus(CreatePostResult.TitleError.NONE);
        }
    }

    public void createPost(String collection, String document, String title, String content, byte[] image, boolean connected) {
        if (title.trim().equals("")) {
            listener.setTitleStatus(CreatePostResult.TitleError.EMPTY);
            listener.setCreateError();
            listener.setLoadingFinished();
        }
        else {
            if (!connected) {
                listener.setErrorMessage("No internet, uploaded without image");
                uploadPost(collection, document, title, content, null, null);
            }
            else if (image == null)
                uploadPost(collection, document, title, content, null, null);
            else {
                Log.d("nowaybro", "Imatge");
                StorageReference storageRef = storage.getReference();
                String randomId = UUID.randomUUID().toString();
                String path;
                if (collection.equals("users"))
                    path = "users/" + username;
                else
                    path = collection + "/" + document;
                String imageId = path + "/" + randomId;
                StorageReference imageRef = storageRef.child(imageId); //Falta comprovar que sigui nou?
                UploadTask uploadTask = imageRef.putBytes(image);
                uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageLink = uri.toString();
                            uploadPost(collection, document, title, content, imageId, imageLink);
                        })
                );
            }
        }
    }

    private void uploadPost(String collection, String document, String title, String content,
                            String imageId, String imageLink) {

        String id = db.collection("users").document().getId();

        DocumentReference docRef;
        if (collection.equals("users")) {
            docRef = db.collection(collection)
                    .document(user)
                    .collection("folders")
                    .document(document)
                    .collection("posts")
                    .document(id);
        } else {
            docRef = db.collection(collection)
                    .document(document)
                    .collection("posts")
                    .document(id);
        }
        docRef.get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                Post post = new Post(title, content, username, id, document, imageId, imageLink);
                docRef.set(post);
                if (collection.equals("groups")) {
                    CollectionReference cr = db.collection(collection).document(document)
                            .collection("members");
                            sendNotification(cr, document, title, imageLink, collection, id);
                } else if (collection.equals("categories")) {
                    CollectionReference cr = db.collection(collection).document(document)
                            .collection("subscribedUsers");
                    sendNotification(cr, document, title, imageLink, collection, id);
                }
                listener.setCreateFinished();
                listener.setLoadingFinished();
            }
        });
    }

    public void sendNotification(CollectionReference collectionReference, String document, String title,
                                 String imageLink, String collection, String id) {
        collectionReference.get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot :
                        Objects.requireNonNull(task1.getResult())) {
                    if (!documentSnapshot.getId().equals(user)) {
                        db.collection(collection).document(document).get()
                                .addOnCompleteListener(task2 -> {
                                    NotificationPost notificationPost = new NotificationPost(
                                            title, Objects.requireNonNull(task2.getResult().get("title")).toString(),
                                            imageLink, false, collection, document, id);
                                    DocumentReference docRef2 = db.collection("users")
                                            .document(documentSnapshot.getId())
                                            .collection("notifications")
                                            .document();
                                    docRef2.set(notificationPost);
                                    docRef2.update("type", "post");
                                    docRef2.update("timestamp", FieldValue.serverTimestamp());
                                });
                    }
                }
            }
        });
    }
}
