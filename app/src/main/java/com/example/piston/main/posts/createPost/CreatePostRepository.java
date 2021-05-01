package com.example.piston.main.posts.createPost;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.piston.data.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CreatePostRepository {
    private final CreatePostRepository.ICreatePost listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String user;
    String id;

    public interface ICreatePost {
        void setTitleStatus(CreatePostResult.TitleError titleError);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
    }

    public CreatePostRepository(CreatePostRepository.ICreatePost listener) {
        this.listener = listener;
        user = auth.getCurrentUser().getEmail();
        Log.d("nowaybro", "Display name: " + auth.getCurrentUser().getDisplayName());
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
                    Post post = new Post(title, content, user, id, "nice",
                            "https://firebasestorage.googleapis.com/v0/b/piston-48298.appspot.com/o/nice?alt=media&token=710e0200-a661-401a-9db3-2b1da7ee2f62");
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
                    Post post = new Post(title, content, user, id, "nice",
                            "https://firebasestorage.googleapis.com/v0/b/piston-48298.appspot.com/o/nice?alt=media&token=710e0200-a661-401a-9db3-2b1da7ee2f62");
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

    public void uploadImage(Bitmap bitmap) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("nice");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
        });
    }
}
