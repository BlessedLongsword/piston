package com.example.piston.main.posts.createPost;

import android.net.Uri;

import com.example.piston.R;
import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.posts.Post;
import com.example.piston.data.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class CreatePostRepository {

    private final CreatePostRepository.ICreatePost listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final String user;
    private String username, profilePictureLink;

    public interface ICreatePost {
        void setTitleStatus(CreatePostResult.TitleError titleError);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
        void setErrorMessage();
    }

    public CreatePostRepository(CreatePostRepository.ICreatePost listener) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        db.collection("users")
                .document(Objects.requireNonNull(user))
                .get()
                .addOnCompleteListener(task -> {
                    username = (String) Objects.requireNonNull(task.getResult()).get("username");
                    profilePictureLink = (String) Objects.requireNonNull(task.getResult()).get("profilePictureLink");
        });

    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreatePostResult.TitleError.EMPTY);
        else {
            listener.setTitleStatus(CreatePostResult.TitleError.NONE);
        }
    }

    public void createPost(String scope, String document, String title, String content, Uri image, boolean connected) {
        if (title.trim().equals("")) {
            listener.setTitleStatus(CreatePostResult.TitleError.EMPTY);
            listener.setCreateError();
            listener.setLoadingFinished();
        }
        else {
            if (!connected) {
                listener.setErrorMessage();
                uploadPost(scope, document, title, content, null);
            }
            else if (image == null)
                uploadPost(scope, document, title, content, null);
            else {
                StorageReference storageRef = storage.getReference();
                String id = db.collection("users").document().getId();
                String path;
                if (scope.equals("folders"))
                    path = "users/" + username;
                else
                    path = scope + "/" + document;

                String imageId = path + "/" + id;
                StorageReference imageRef = storageRef.child(imageId);
                UploadTask uploadTask = imageRef.putFile(image);
                uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageLink = uri.toString();
                            uploadPost(scope, document, title, content, imageLink);
                        })
                );
            }
        }
    }

    private void uploadPost(String scope, String sectionID, String title, String content,
                            String imageLink) {

        String id = db.collection("users").document().getId();

        DocumentReference docRef;
        if (scope.equals("folders")) {
            docRef = db.collection("users")
                    .document(user)
                    .collection("folders")
                    .document(sectionID)
                    .collection("posts")
                    .document(id);
        } else {
            docRef = db.collection(scope)
                    .document(sectionID)
                    .collection("posts")
                    .document(id);
        }
        docRef.get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                Post post = new Post(id, username, user, content, title, sectionID, imageLink, profilePictureLink);
                docRef.set(post);
                FieldValue timestamp = FieldValue.serverTimestamp();
                docRef.update("timestamp", timestamp);
                DocumentReference sectionDocRef = db.collection(scope).document(sectionID);
                if (scope.equals("groups")) {
                    CollectionReference cr = sectionDocRef.collection("members");
                    sendNotification(cr, sectionID, title, imageLink, scope, id);
                } else if (scope.equals("categories")) {
                    CollectionReference cr = sectionDocRef.collection("subscribedUsers");
                    sendNotification(cr, sectionID, title, imageLink, scope, id);
                }
                sectionDocRef.update("timestamp", timestamp);
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
                                            imageLink, (String) task2.getResult().get("imageLink"), false, collection, document, id);

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
