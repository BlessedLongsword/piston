package com.example.piston.data.repositories;

import com.example.piston.data.CreateCategoryResult;
import com.example.piston.data.RegisterResult;
import com.example.piston.data.Section;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateCategoryRepository {

    private final ICreateCategory listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface ICreateCategory {
        void setTitleStatus(CreateCategoryResult.TitleError titleError);
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

    public void createCategory(String title, String description) {
        DocumentReference docRef = db.collection("categories").document(title);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                DocumentSnapshot ds = task.getResult();
                if (ds.exists())
                    listener.setTitleStatus(CreateCategoryResult.TitleError.EXISTS);
                else {
                    Section category = new Section(title, description);
                    db.collection("categories").document().set(category);
                    listener.setLoadingFinished();
                }
            }
        });
    }

}
