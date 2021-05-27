package com.example.piston.main.global;

import com.example.piston.data.sections.Category;
import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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

    private Query categoriesQuery, base;
    private Category[] categories;
    private Boolean[] subscriptions;
    private int counter, lastRequest = 0;
    private String filter;

    public interface IGlobal {
        void setCategories(ArrayList<Category> categories);
        void setSubscribed(ArrayList<Boolean> subscriptions);
        void setIsAdmin(boolean isAdmin);
        void setFilter(String filter);
    }

    public GlobalRepository(IGlobal listener, boolean nsfw) {
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

        nsfwBase(nsfw);
        categoriesQuery = base.orderBy("timestamp", Query.Direction.DESCENDING);

        listenChanges();

    }

    private void loadCategories() {

        categoriesQuery.get().addOnCompleteListener(task -> {
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
                Category[] finalCategories = new Category[categories.length];
                Category[] subsCategories = new Category[categories.length];
                Category[] nonSubsCategories = new Category[categories.length];
                int subCount = 0, nonSubCount = 0;
                for (int i = 0; i < categories.length; i++) {
                    if (subscriptions[i]) {
                        subsCategories[subCount] = categories[i];
                        subCount++;
                    } else {
                        nonSubsCategories[nonSubCount] = categories[i];
                        nonSubCount++;
                    }
                }
                for (int i = 0; i < subCount; i++)
                    subscriptions[i] = true;
                for (int i = subCount; i < categories.length; i++)
                    subscriptions[i] = false;
                System.arraycopy(subsCategories, 0, finalCategories, 0, subCount);
                System.arraycopy(nonSubsCategories, 0, finalCategories, subCount, nonSubCount);
                listener.setCategories(new ArrayList<>(Arrays.asList(finalCategories)));
                listener.setSubscribed(new ArrayList<>(Arrays.asList(subscriptions)));
            }
        }
    }

    public void addSub(boolean sub, String id){
        DocumentReference categoryDocRef = db.collection("categories").document(id);
        DocumentReference userDocRef = db.collection("users")
                .document(Objects.requireNonNull(email)).collection("subscribedCategories")
                .document(id);
        categoryDocRef.get().addOnCompleteListener(task -> {
            long numSubs = (long) Objects.requireNonNull(task.getResult().get("numSubs"));
            if (task.isSuccessful()) {
                if (sub) {
                    Map<String, String> data = new HashMap<>();
                    data.put("title", id);
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

    public void updateQuery(String field, boolean descending, boolean nsfw) {
        filter = field;
        nsfwBase(nsfw);
        if (field.equals(Values.FILTER_DEFAULT))
            categoriesQuery = base.orderBy("timestamp", Query.Direction.DESCENDING);
        else
            categoriesQuery = base.orderBy(field,
                    (descending) ? Query.Direction.DESCENDING : Query.Direction.ASCENDING)
                    .orderBy("timestamp", Query.Direction.DESCENDING);

        loadCategories();
        listener.setFilter(field);
    }

    public void showNsfw(boolean nsfw) {
        nsfwBase(nsfw);
        if (filter != null) {
            if (filter.equals(Values.FILTER_MOST_SUBSCRIBERS))
                categoriesQuery = base.orderBy(filter, Query.Direction.DESCENDING)
                        .orderBy("timestamp", Query.Direction.DESCENDING);
            else
                categoriesQuery = base.orderBy(filter, Query.Direction.ASCENDING)
                        .orderBy("timestamp", Query.Direction.DESCENDING);
        }
        else
            categoriesQuery = base.orderBy("timestamp", Query.Direction.DESCENDING);
        loadCategories();
    }

    private void nsfwBase(boolean nsfw) {
        if (nsfw)
            base = db.collection(Values.GLOBAL);
        else
            base = db.collection(Values.GLOBAL).whereEqualTo("nsfw", false);
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
