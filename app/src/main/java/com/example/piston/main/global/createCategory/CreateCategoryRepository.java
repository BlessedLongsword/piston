package com.example.piston.main.global.createCategory;

import android.net.Uri;

import com.example.piston.data.sections.Category;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class CreateCategoryRepository {

    private final ICreateCategory listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public interface ICreateCategory {
        void setTitleStatus(CreateCategoryResult.TitleError titleError);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
        void setImageError();
    }

    public CreateCategoryRepository(ICreateCategory listener) {
        this.listener = listener;
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreateCategoryResult.TitleError.EMPTY);
        else {
            DocumentReference docRef = db.collection("categories").document(title);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    DocumentSnapshot ds = task.getResult();
                    if (Objects.requireNonNull(ds).exists())
                        listener.setTitleStatus(CreateCategoryResult.TitleError.EXISTS);
                    else
                        listener.setTitleStatus(CreateCategoryResult.TitleError.NONE);
                }
            });
        }
    }

    public void createCategory(String title, String description, boolean nsfw, Uri image, boolean connected) {
        if (title.trim().equals("")) {
            listener.setTitleStatus(CreateCategoryResult.TitleError.EMPTY);
            listener.setCreateError();
            listener.setLoadingFinished();
        }
        else if (!connected || image == null) {
            listener.setImageError();
            listener.setLoadingFinished();
        }
        else {
            StorageReference storageRef = storage.getReference();
            String id = UUID.randomUUID().toString(); //Check if it's new?
            String path = "categories/" + id;
            String imageId = path + "/" + "categoryImage";
            StorageReference imageRef = storageRef.child(imageId);
            UploadTask uploadTask = imageRef.putFile(image);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads?
            }).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        String imageLink = uri.toString();
                        DocumentReference docRef = db.collection("categories").document(id);
                        docRef.get().addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                DocumentSnapshot ds = task.getResult();
                                if (Objects.requireNonNull(ds).exists()) {
                                    listener.setTitleStatus(CreateCategoryResult.TitleError.EXISTS);
                                    listener.setCreateError();
                                } else {
                                    Category category = new Category(id, title, description, imageLink, nsfw);
                                    db.collection("categories").document(id).set(category);
                                    listener.setCreateFinished();
                                }
                                listener.setLoadingFinished();
                            }
                        });
                    }));
        }
    }
}
