package com.example.piston.main.posts.createPost;

import android.net.Uri;

import com.example.piston.data.notifications.NotificationPost;
import com.example.piston.data.posts.Post;
import com.example.piston.utilities.Values;
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
    private final String email;
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
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        db.collection("users")
                .document(Objects.requireNonNull(email))
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

    public void createPost(String scope, String sectionID, String title, String content,
                           Uri image, boolean connected) {
        if (title.trim().equals("")) {
            listener.setTitleStatus(CreatePostResult.TitleError.EMPTY);
            listener.setCreateError();
            listener.setLoadingFinished();
        }
        else {
            DocumentReference base = FirebaseFirestore.getInstance().document("");
            if (scope.equals(Values.PERSONAL))
                base = base.collection("users").document(email);
            CollectionReference parent = base.collection(scope).document(sectionID).
                    collection("posts");
            DocumentReference postReference = parent.document();
            String postID = postReference.getId();

            if (!connected) {
                listener.setErrorMessage();
                uploadPost(scope, sectionID, postID, title, content, null, postReference);
            }
            else if (image == null)
                uploadPost(scope, sectionID, postID, title, content, null, postReference);
            else {
                StorageReference storageBase;
                if (scope.equals(Values.PERSONAL))
                    storageBase = storage.getReference().child("users").child(email);
                else
                    storageBase = storage.getReference().child(scope);
                StorageReference storagePost = storageBase.child(sectionID).child(postID);

                UploadTask uploadTask = storagePost.putFile(image);
                uploadTask.addOnSuccessListener(taskSnapshot -> storagePost.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    String imageLink = uri.toString();
                    uploadPost(scope, sectionID, postID, title, content, imageLink, postReference);
                }));
            }
        }
    }

    private void uploadPost(String scope, String sectionID, String postID, String title,
                            String content, String imageLink, DocumentReference postReference) {

        postReference.get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                Post post = new Post(postID, username, email, content, title, sectionID, imageLink,
                        profilePictureLink);
                postReference.set(post);
                FieldValue timestamp = FieldValue.serverTimestamp();
                postReference.update("timestamp", timestamp);

                DocumentReference sectionDocRef = db.collection(scope).document(sectionID);
                if (scope.equals(Values.GROUPS)) {
                    CollectionReference cr = sectionDocRef.collection("members");
                    sendNotification(cr, sectionID, title, imageLink, scope, postID, timestamp);
                } else if (scope.equals(Values.GLOBAL)) {
                    CollectionReference cr = sectionDocRef.collection("subscribedUsers");
                    sendNotification(cr, sectionID, title, imageLink, scope, postID, timestamp);
                    sectionDocRef.update("timestamp", timestamp);
                }
                listener.setCreateFinished();
                listener.setLoadingFinished();
            }
        });
    }

    public void sendNotification(CollectionReference collectionReference, String document, String title,
                                 String imageLink, String collection, String id, FieldValue timestamp) {
        collectionReference.get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot :
                        Objects.requireNonNull(task1.getResult())) {
                    if (!documentSnapshot.getId().equals(email)) {
                        db.collection(collection).document(document).get()
                                .addOnCompleteListener(task2 -> {
                                    DocumentReference docRef2 = db.collection("users")
                                            .document(documentSnapshot.getId())
                                            .collection("notifications")
                                            .document();
                                    String notificationID = docRef2.getId();
                                    NotificationPost notificationPost = new NotificationPost(
                                            title, Objects.requireNonNull(task2.getResult().get("title")).toString(),
                                            imageLink, (String) task2.getResult().get("imageLink"), false, collection, document, id, notificationID);

                                    docRef2.set(notificationPost);
                                    docRef2.update("type", "post");
                                    docRef2.update("timestamp", timestamp);
                                });
                    }
                    if (collection.equals(Values.GROUPS)) {
                        db.collection("users").document(documentSnapshot.getId())
                                .collection(Values.GROUPS).document(document).update("timestamp", timestamp);
                    }
                }
            }
        });
    }
}
