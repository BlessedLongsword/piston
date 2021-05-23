package com.example.piston.main.global.category.info;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CategoryInfoRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference categoryDocRef;
    private final String category, email;

    final ICategoryInfo listener;

    public interface ICategoryInfo {
        void setParams(String title, String description, String imageLink);
        void setSubscribed(boolean subscribed);
        void setIsAdmin(boolean admin);
        void setFinished();
    }

    public CategoryInfoRepository(ICategoryInfo listener, String category) {
        this.listener = listener;
        this.category = category;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        this.email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        categoryDocRef = db.collection("categories").document(category);

        updateParams();
        isAdmin();
    }

    public void addSub(boolean sub){
        DocumentReference categoryDocRef = db.collection("categories").document(category);
        DocumentReference userDocRef = db.collection("users")
                .document(Objects.requireNonNull(email)).collection("subscribedCategories")
                .document(category);
        categoryDocRef.get().addOnCompleteListener(task -> {
            long numSubs = (long) Objects.requireNonNull(task.getResult().get("numSubs"));
            if (task.isSuccessful()) {
                if (sub) {
                    Map<String, String> data = new HashMap<>();
                    data.put("title", category);
                    userDocRef.set(data);
                    Map<String, String> data2 = new HashMap<>();
                    data2.put("email", email);
                    categoryDocRef.update("numSubs", ++numSubs);
                    categoryDocRef.collection("subscribedUsers").document(email).set(data2);
                } else {
                    categoryDocRef.update("numSubs", --numSubs);
                    categoryDocRef.collection("subscribedUsers").document(email).delete();
                    userDocRef.delete();
                }
            }
        });
    }

    public void checkSub () {
        categoryDocRef.collection("subscribedUsers").document(email)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        listener.setSubscribed(task.getResult().exists());
                    }
        });
    }

    public void deleteCategory() {
        deleteSubscribedUsers(categoryDocRef.getId());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("categories").child(categoryDocRef.getId());

        categoryDocRef.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Delete posts inside category
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    String Id = documentSnapshot.getId();
                    DocumentReference docRef1 = categoryDocRef.collection("posts").document(Id);
                    deleteSubscribedUsers(Id);

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

                    // Delete userLikes
                    docRef1.collection("userLikes")
                            .get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot1 : Objects.requireNonNull(
                                    task2.getResult())) {
                                db.collection("users").document(snapshot1.getId())
                                        .collection("liked")
                                        .document(Id)
                                        .delete();

                                docRef1.collection("userLikes")
                                        .document(snapshot1.getId())
                                        .delete();
                            }
                        }
                    });

                    storageReference.child(Id).delete();

                    docRef1.delete();
                }
            }
        });
        storageReference.child("categoryImage").delete();
        categoryDocRef.delete(); // Delete category
    }

    private void deleteSubscribedUsers (String categoryId) {
        categoryDocRef.collection("subscribedUsers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Remove subscribedCategories from user
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    db.collection("users")
                            .document(documentSnapshot.getId())
                            .collection("subscribedCategories")
                            .document(categoryId).delete();

                    categoryDocRef.collection("subscribedUsers")
                            .document(documentSnapshot.getId())
                            .delete();
                }
            }
        });

    }

    private void isAdmin() {
        db.collection("admins").document(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setIsAdmin(task.getResult().exists());
            }
        });
    }

    public void editDescription(String text) {
        categoryDocRef.get().addOnSuccessListener(documentSnapshot -> {
            categoryDocRef.update("description", text);
            listener.setFinished();
        });
    }

    public void editTitle(String text) {
        categoryDocRef.get().addOnSuccessListener(documentSnapshot -> {
            categoryDocRef.update("title", text);
            listener.setFinished();
        });
    }

    public void updateParams() {
        categoryDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setParams((String) Objects.requireNonNull(task.getResult()).get("title"),
                        (String) task.getResult().get("description"),
                        (String) task.getResult().get("imageLink"));
            }
        });
    }

}
