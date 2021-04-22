package com.example.piston.data.repositories;

import android.util.Log;

import com.example.piston.data.Folder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class PersonalRepository {

    private final PersonalRepository.IPersonal listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String user;

    public interface IPersonal {
        void setFolders(ArrayList<Folder> categories);
    }

    public PersonalRepository(PersonalRepository.IPersonal listener) {
        this.listener = listener;
        user = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail());
        listenChanges();
    }

    public void loadFolders() {

        db.collection("users")
                .document(user)
                .collection("folders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Folder> folders = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                                task.getResult())) {
                            Folder Folder = documentSnapshot.toObject(Folder.class);
                            folders.add(Folder);
                        }
                        listener.setFolders(folders);
                    } else {
                        Log.d("nowaybro", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void listenChanges() {
        db.collection("users")
                .document(user)
                .collection("folders")
                .addSnapshotListener((snapshots, e) -> {
                    PersonalRepository.this.loadFolders();
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
