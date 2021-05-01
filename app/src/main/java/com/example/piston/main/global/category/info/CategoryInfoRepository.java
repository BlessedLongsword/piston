package com.example.piston.main.global.category.info;

import com.google.firebase.firestore.FirebaseFirestore;

public class CategoryInfoRepository {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ICategoryInfo listener;

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
                        listener.setDescription((String) task.getResult().get("description"));
                        listener.setImageLink((String) task.getResult().get("imageLink"));
                    }
        });
    }

}
