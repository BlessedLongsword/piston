package com.example.piston.main.global;

import android.util.Log;

import com.example.piston.data.Category;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class GlobalRepository {

    private final IGlobal listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        db.collection("categories")
                .addSnapshotListener((snapshots, e) -> {
                    GlobalRepository.this.loadCategories();
                    /*if (e != null) {
                        Log.w("nowaybro", "listen:error", e);
                        return;
                    }
                    for (DocumentChange dc : Objects.requireNonNull(snapshots).getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                Log.d(TAG, "New city: " + dc.getDocument().getData());
                                break;
                            case MODIFIED:
                                Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                break;
                            case REMOVED:
                                Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                break;
                        }
                    }*/
                });
    }

}
