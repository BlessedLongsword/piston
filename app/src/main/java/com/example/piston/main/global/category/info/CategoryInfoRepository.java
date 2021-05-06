package com.example.piston.main.global.category.info;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class CategoryInfoRepository {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    final ICategoryInfo listener;

    public interface ICategoryInfo {
        void setDescription(String description);
        void setImageLink(String imageLink);
    }

    public CategoryInfoRepository(ICategoryInfo listener, String category) {
        this.listener = listener;
        db.collection("categories")
                .document(category)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.setDescription((String) Objects.requireNonNull(task.getResult()).get("description"));
                        listener.setImageLink((String) task.getResult().get("imageLink"));
                    }
        });
    }

}
