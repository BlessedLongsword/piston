package com.example.piston.main.global.category.info;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CategoryInfoRepository {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String category;

    final ICategoryInfo listener;

    public interface ICategoryInfo {
        void setDescription(String description);
        void setImageLink(String imageLink);
        void setSubscribed(boolean subscribed);
    }

    public CategoryInfoRepository(ICategoryInfo listener, String category) {
        this.listener = listener;
        this.category = category;
        db.collection("categories")
                .document(category)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.setDescription((String) Objects.requireNonNull(task.getResult()).get("description"));
                        listener.setImageLink((String) task.getResult().get("imageLink"));
                    }
        });
    }

    public void addSub(boolean sub){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        if (sub){
            Map<String, String> data = new HashMap<>();
            data.put("title", category);
            db.collection("users").document(Objects.requireNonNull(email)).collection("subscribedCategories").document(category).set(data);
            Map<String, String> data2 = new HashMap<>();
            data2.put("email", email);
            db.collection("categories").document(category).collection("subscribedUsers").document(email).set(data2);
        }
        else{
            db.collection("users").document(Objects.requireNonNull(email)).collection("subscribedCategories").document(category).delete();
            db.collection("categories").document(category).collection("subscribedUsers").document(email).delete();
        }
    }

    public void checkSub () {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail());
        DocumentReference dR = db.collection("categories")
                .document(category).collection("subscribedUsers").document(email);
        dR.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot ds = task.getResult();
                listener.setSubscribed(ds.exists());
            }
        });
    }

}
