package com.example.piston.main.global;

import android.util.Log;

import com.example.piston.data.Category;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class GlobalRepository {

    private final IGlobal listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistration;

    public interface IGlobal {
        void setCategories(ArrayList<Category> categories);
    }

    public GlobalRepository(IGlobal listener) {
        this.listener = listener;
        listenChanges();
    }

    public void loadCategories() {
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Category> categories = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            Category category = documentSnapshot.toObject(Category.class);
                            categories.add(category);
                        }
                        listener.setCategories(categories);
                    } else {
                        Log.d("nowaybro", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void listenChanges() {
        listenerRegistration = db.collection("categories")
                .addSnapshotListener((snapshots, e) -> {
                    GlobalRepository.this.loadCategories();
                });
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
