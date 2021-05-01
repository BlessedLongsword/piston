package com.example.piston.main.global.createCategory;

import com.example.piston.data.Category;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CreateCategoryRepository {

    private final ICreateCategory listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private String imageId, imageLink;

    public interface ICreateCategory {
        void setTitleStatus(CreateCategoryResult.TitleError titleError);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
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
                    if (ds.exists())
                        listener.setTitleStatus(CreateCategoryResult.TitleError.EXISTS);
                    else
                        listener.setTitleStatus(CreateCategoryResult.TitleError.NONE);
                }
            });
        }
    }

    public void createCategory(String title, String description, boolean nsfw) {
        if (title.trim().equals("")) {
            listener.setTitleStatus(CreateCategoryResult.TitleError.EMPTY);
            listener.setCreateError();
            listener.setLoadingFinished();
        } else {
            DocumentReference docRef = db.collection("categories").document(title);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds.exists()) {
                        listener.setTitleStatus(CreateCategoryResult.TitleError.EXISTS);
                        listener.setCreateError();
                        listener.setLoadingFinished();
                    } else {
                        Category category = new Category(title, description, nsfw, imageId, imageLink);
                        db.collection("categories").document(title).set(category);
                        listener.setCreateFinished();
                        listener.setLoadingFinished();
                    }
                }
            });
        }
    }

    public void uploadImage(byte[] image) {
        StorageReference storageRef = storage.getReference();
        imageId = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child(imageId); //Falta comprovar que sigui nou
        UploadTask uploadTask = imageRef.putBytes(image);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> imageLink = uri.toString()));
    }

}
