package com.example.piston.main.global.category.info;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CategoryInfoRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference docRef, docRef1;
    private final String category, email;

    final ICategoryInfo listener;

    public interface ICategoryInfo {
        void setParams(String title, String description, String imageLink);
        void setSubscribed(boolean subscribed);
        void setIsAdmin(boolean admin);
    }

    public CategoryInfoRepository(ICategoryInfo listener, String category) {
        this.listener = listener;
        this.category = category;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        docRef = db.collection("categories").document(category);
        docRef1 = db.collection("users").document(Objects.requireNonNull(email));

        docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.setParams((String) Objects.requireNonNull(task.getResult()).get("title"),
                            (String) task.getResult().get("description"),
                            (String) task.getResult().get("imageLink"));
                }
        });

        isAdmin();
    }

    public void addSub(boolean sub){
        if (sub){
            Map<String, String> data = new HashMap<>();
            data.put("id", category);
            docRef1.collection("subscribedCategories").document(category).set(data);
            Map<String, String> data2 = new HashMap<>();
            data2.put("email", email);
            docRef.collection("subscribedUsers").document(email).set(data2);
        }
        else{
            docRef1.collection("subscribedCategories").document(category).delete();
            docRef.collection("subscribedUsers").document(email).delete();
        }
    }

    public void checkSub () {
        DocumentReference dR = docRef.collection("subscribedUsers").document(email);
        dR.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot ds = task.getResult();
                listener.setSubscribed(ds.exists());
            }
        });
    }

    public void deleteCategory() {
        docRef.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Delete posts inside category
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    String categoryId = documentSnapshot.getId();
                    deleteSubscribedUsers(categoryId);
                    DocumentReference docRef1 = docRef.collection("posts").document(categoryId);

                    // Delete replies inside post
                    docRef1.collection("replies")
                            .get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot1 : Objects.requireNonNull(
                                    task1.getResult())) {
                                docRef1.collection("replies")
                                        .document(snapshot1.getId())
                                        .delete();
                            }
                        }
                    });
                    docRef1.delete();
                }
            }
        });
        docRef.delete(); // Delete category
    }

    private void deleteSubscribedUsers (String categoryId) {
        docRef.collection("subscribedUsers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Remove liked post from User's collection
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    DocumentReference docRef1 = db.collection("users")
                            .document(documentSnapshot.getId())
                            .collection("subscribedCategories")
                            .document(categoryId);

                    docRef1.get().addOnCompleteListener(task1 -> {
                        if (task1.getResult().exists())
                            docRef1.delete();
                    });

                }
            }
        });
    }

    private void isAdmin() {
        db.collection("admins").document(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setIsAdmin(true);
            }
        });
    }

}
