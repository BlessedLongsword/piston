package com.example.piston.main.posts.createPost;

import android.util.Log;

import com.example.piston.data.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreatePostRepository {
    private final CreatePostRepository.ICreatePost listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String user;
    String username;
    String id;
    String imageId;
    String imageLink;

    public interface ICreatePost {
        void setTitleStatus(CreatePostResult.TitleError titleError);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
    }

    public CreatePostRepository(CreatePostRepository.ICreatePost listener) {
        this.listener = listener;
        user = auth.getCurrentUser().getEmail();
        db.collection("users")
                .document(user)
                .get()
                .addOnCompleteListener(task ->
                        username = (String) task.getResult().get("username"));
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreatePostResult.TitleError.EMPTY);
        else {
            listener.setTitleStatus(CreatePostResult.TitleError.NONE);
        }
    }

    public void createPost(String collection, String document, String title, String content) {
        if (title.trim().equals("")) {
            listener.setTitleStatus(CreatePostResult.TitleError.EMPTY);
            listener.setCreateError();
            listener.setLoadingFinished();
        }
        //Personal
        else if (collection.equals("users")) {
            DocumentReference docRef = db.collection(collection)
                    .document(user)
                    .collection("folders")
                    .document(document)
                    .collection("posts")
                    .document(id);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Post post = new Post(title, content, username, id, imageId, imageLink);
                    docRef.set(post);
                    listener.setCreateFinished();
                    listener.setLoadingFinished();
                    }
            });
        }
        //Group || Category
        else {
            DocumentReference docRef = db.collection(collection)
                    .document(document)
                    .collection("posts")
                    .document(id);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Post post = new Post(title, content, username, id, imageId, imageLink);
                    docRef.set(post);
                    listener.setCreateFinished();
                    listener.setLoadingFinished();
                }
            });
        }
    }

    public void generatePostID() {
        Map data = new HashMap();
        data.put("Null", null);
        db.collection("users")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    id = documentReference.getId();
                    Log.d("what", "The id is: " + documentReference.getId());
                    db.collection("users")
                            .document(id)
                            .delete().addOnCompleteListener(task -> {
                                listener.setLoadingFinished();
                    });
                });
    }

    public void uploadImage(byte[] image) {
        StorageReference storageRef = storage.getReference();
        imageId = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child(id); //Falta comprovar que sigui nou
        UploadTask uploadTask = imageRef.putBytes(image);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> imageLink = uri.toString()));
    }
}