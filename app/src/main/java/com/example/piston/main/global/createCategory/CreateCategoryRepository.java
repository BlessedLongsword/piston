package com.example.piston.main.global.createCategory;

import android.net.Uri;
import android.util.Log;

import com.example.piston.data.sections.Category;
import com.example.piston.utilities.Values;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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

    public void createCategory(String title, String description, boolean nsfw,
                               Uri image, boolean connected) {

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
            db.collection(Values.GLOBAL).whereEqualTo("title", title).get()
                    .addOnCompleteListener(task -> {
                        if (!task.getResult().isEmpty()) {
                            listener.setTitleStatus(CreateCategoryResult.TitleError.EXISTS);
                            listener.setCreateError();
                            listener.setLoadingFinished();
                        }
                        else {
                            createCategory(title, description, nsfw, image);
                        }
                    });

        }
    }

    private void createCategory(String title, String description, boolean nsfw, Uri image) {
        DocumentReference categoryReference = db.collection(Values.GLOBAL).document();
        String categoryID = categoryReference.getId();

        StorageReference categoryStorage = storage.getReference().child(Values.GLOBAL).
                child(categoryID).child("categoryImage");

        UploadTask uploadTask = categoryStorage.putFile(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> categoryStorage.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    String imageLink = uri.toString();
                    Category category = new Category(categoryID, title, description, imageLink, nsfw);
                    categoryReference.set(category);
                    categoryReference.update("timestamp", FieldValue.serverTimestamp());
                    listener.setLoadingFinished();
                    listener.setCreateFinished();
                }));
    }
}
