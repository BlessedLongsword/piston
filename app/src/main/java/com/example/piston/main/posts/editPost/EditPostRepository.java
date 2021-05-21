package com.example.piston.main.posts.editPost;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static java.lang.Thread.sleep;

public class EditPostRepository {

    private final EditPostRepository.IEditPost listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final String email, scope, sectionID, postID;
    private final DocumentReference postDocRef;

    private String username;

    public interface IEditPost {
        void setTitleStatus(EditPostResult.TitleError titleError);
        void setParams(String title, String content, String image);
        void setEditError();
        void setEditFinished();
        void setLoadingFinished();
        void setErrorMessage();
    }

    public EditPostRepository(EditPostRepository.IEditPost listener, String scope, String sectionID, String postID) {
        this.listener = listener;
        this.scope = scope;
        this.sectionID = sectionID;
        this.postID = postID;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();

        postDocRef = getPostDocRef();

        postDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setParams((String) task.getResult().get("title"),
                        (String) task.getResult().get("content"),
                        (String) task.getResult().get("imageLink"));

                username = (String) task.getResult().get("owner");
            }
        });

    }

    public void editPost(String title, String content, Uri image, boolean connected) {
        if (title.trim().equals("")) {
            listener.setTitleStatus(EditPostResult.TitleError.EMPTY);
            listener.setEditError();
            listener.setLoadingFinished();
        }
        else {
            if (!connected) {
                listener.setErrorMessage();
                updatePost(title, content, null);
            }
            else if (image == null)
                updatePost(title, content, null);
            else {
                StorageReference storageRef = storage.getReference();
                String id = db.collection("users").document().getId();
                String path;
                if (scope.equals("folders"))
                    path = "users/" + username;
                else
                    path = scope + "/" + sectionID;

                String imageId = path + "/" + id;
                StorageReference imageRef = storageRef.child(imageId);
                UploadTask uploadTask = imageRef.putFile(image);
                uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageLink = uri.toString();
                            updatePost(title, content, imageLink);
                        })
                );
            }
        }
    }

    private void updatePost(String title, String content, String imageLink) {
        postDocRef.get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                postDocRef.update("timestamp", FieldValue.serverTimestamp());
                postDocRef.update("title", title);
                postDocRef.update("imageLink", imageLink);
                postDocRef.update("content", content);

                listener.setEditFinished();
                listener.setLoadingFinished();
            }
        });
    }

    private DocumentReference getPostDocRef () {
        DocumentReference docRef;
        if (scope.equals("folders")) {
            docRef = db.collection("users")
                    .document(email)
                    .collection("folders")
                    .document(sectionID)
                    .collection("posts")
                    .document(postID);
        } else {
            docRef = db.collection(scope)
                    .document(sectionID)
                    .collection("posts")
                    .document(postID);
        }
        return docRef;
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(EditPostResult.TitleError.EMPTY);
        else {
            listener.setTitleStatus(EditPostResult.TitleError.NONE);
        }
    }
}
