package com.example.piston.main.personal;

import android.util.Log;

import com.example.piston.data.Folder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class PersonalRepository {

    private final PersonalRepository.IPersonal listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String user;
    private ListenerRegistration listenerRegistration;

    public interface IPersonal {
        void setFolders(ArrayList<Folder> categories);
    }

    public PersonalRepository(PersonalRepository.IPersonal listener) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
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
        listenerRegistration = db.collection("users")
                .document(user)
                .collection("folders")
                .addSnapshotListener((snapshots, e) -> PersonalRepository.this.loadFolders());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
