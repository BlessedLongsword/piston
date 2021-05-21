package com.example.piston.main.global;

import android.util.Log;

import com.example.piston.data.sections.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GlobalRepository {

    private final IGlobal listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String email;
    private ListenerRegistration listenerRegistrationCategories, listenerRegistrationSubs;

    private Query categoriesQuery;
    private Category[] categories;
    private Boolean[] subscriptions;
    private int counter;
    private int lastRequest = 0;

    public interface IGlobal {
        void setCategories(ArrayList<Category> categories);
        void setSubscribed(ArrayList<Boolean> subscriptions);
        void setIsAdmin(boolean isAdmin);
    }

    public GlobalRepository(IGlobal listener) {
        this.listener = listener;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection("admins")
                .document(Objects.requireNonNull(Objects.requireNonNull
                        (mAuth.getCurrentUser()).getEmail()))
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        listener.setIsAdmin(task.getResult().exists());
                });
        listenChanges();
        categoriesQuery =  db.collection("categories").orderBy("timestamp",
                Query.Direction.DESCENDING);
    }

    private void loadCategories() {
                categoriesQuery.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int size = task.getResult().size();
                        counter = 0;
                        categories = new Category[size];
                        subscriptions = new Boolean[size];
                        final int requestNumber = ++lastRequest;
                        int position = 0;
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            final int positionActual = position++;
                            db.collection("categories")
                                    .document(documentSnapshot.getId())
                                    .collection("subscribedUsers")
                                    .document(Objects.requireNonNull(email))
                                    .get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            addCategory(positionActual,
                                                    documentSnapshot.toObject(Category.class),
                                                    task1.getResult().exists(), requestNumber);
                                        }
                                    });
                        }
                    }
                });
    }

    private void addCategory(int position, Category category, Boolean isSubbed, int requestNumber) {
        if (requestNumber == lastRequest) {
            categories[position] = category;
            subscriptions[position] = isSubbed;
            if (++counter == categories.length) {
                listener.setCategories(new ArrayList<>(Arrays.asList(categories)));
                listener.setSubscribed(new ArrayList<>(Arrays.asList(subscriptions)));
            }
        }
    }

    public void addSub(boolean sub, String id){
        if (sub){
            Map<String, String> data = new HashMap<>();
            data.put("title", id);
            db.collection("users")
                    .document(Objects.requireNonNull(email)).collection("subscribedCategories")
                    .document(id).set(data);
            Map<String, String> data2 = new HashMap<>();
            data2.put("email", email);
            db.collection("categories").document(id)
                    .collection("subscribedUsers").document(email).set(data2);
        }
        else{
            db.collection("categories").document(id)
                    .collection("subscribedUsers").document(email).delete();
            db.collection("users").document(Objects.requireNonNull(email))
                    .collection("subscribedCategories").document(id).delete();
        }
    }

    public void updateQuery(String field) {
        categoriesQuery = db.collection("categories").orderBy("timestamp",
                Query.Direction.DESCENDING).orderBy(field);
        loadCategories();
    }

    private void listenChanges() {
        listenerRegistrationCategories = db.collection("categories")
                .addSnapshotListener((snapshots, e) -> GlobalRepository.this.loadCategories());
        listenerRegistrationSubs = db.collection("users")
                .document(email)
                .collection("subscribedCategories")
                .addSnapshotListener((snapshots, e) -> GlobalRepository.this.loadCategories());
    }

    public void removeListener() {
        listenerRegistrationCategories.remove();
        listenerRegistrationSubs.remove();
    }

}
