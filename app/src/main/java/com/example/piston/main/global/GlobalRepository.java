package com.example.piston.main.global;

import android.util.Log;

import com.example.piston.data.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GlobalRepository {

    private final IGlobal listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistration;

    public interface IGlobal {
        void setCategories(ArrayList<Category> categories);
        void setSubscribed(HashMap<Integer,Boolean> subs);
    }

    public GlobalRepository(IGlobal listener) {
        this.listener = listener;
        listenChanges();
    }

    private void loadCategories() {
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Category> categories = new ArrayList<>();
                        HashMap<Integer, Boolean> subscribed = new HashMap<>();
                        Integer count = 0;
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            Category category = documentSnapshot.toObject(Category.class);
                            categories.add(category);
                            checkSub(subscribed, count, category.getTitle());
                            count++;
                        }
                        listener.setCategories(categories);
                    } else {
                        Log.d("nowaybro", "Error getting documents: ", task.getException());
                    }
                });
    }

    public void addSub(boolean sub, String title){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        if (sub){
            Map<String, String> data = new HashMap<>();
            data.put("title", title);
            db.collection("users").document(email).collection("subscribed").document(title).set(data);
            Map<String, String> data2 = new HashMap<>();
            data2.put("email", email);
            db.collection("categories").document(title).collection("subs").document(email).set(data2);
        }
        else{
            db.collection("users").document(email).collection("subscribed").document(title).delete();
            db.collection("categories").document(title).collection("subs").document(email).delete();
        }
    }

    public void checkSub (HashMap<Integer, Boolean> map, int position, String title){
        //En caso de que no utilizemos la lista de suscripciones pues se cambia esto
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        DocumentReference dR = db.collection("categories").document(title).collection("subs").document(Objects.requireNonNull(email));
        dR.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                map.put(position,task.getResult().exists());
        });


    }
    private void listenChanges() {
        listenerRegistration = db.collection("categories")
                .addSnapshotListener((snapshots, e) -> GlobalRepository.this.loadCategories());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }

}
