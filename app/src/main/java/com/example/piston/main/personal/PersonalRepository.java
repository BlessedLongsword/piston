package com.example.piston.main.personal;

import com.example.piston.data.sections.Folder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class PersonalRepository {

    private final PersonalRepository.IPersonal listener;
    private ListenerRegistration listenerRegistration;
    private final Query foldersQuery;

    public interface IPersonal {
        void setFolders(ArrayList<Folder> categories);
    }

    public PersonalRepository(PersonalRepository.IPersonal listener) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = Objects.requireNonNull(auth.getCurrentUser()).getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        foldersQuery = db.collection("users")
                .document(Objects.requireNonNull(user))
                .collection("folders")
                .orderBy("title")
                .orderBy("timestamp");

        listenChanges();
    }

    private void loadFolders() {
        foldersQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Folder> folders = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    Folder Folder = documentSnapshot.toObject(Folder.class);
                    folders.add(Folder);
                }
                listener.setFolders(folders);
            }
        });
    }

    private void listenChanges() {
        listenerRegistration = foldersQuery.addSnapshotListener(
                (snapshots, e) -> PersonalRepository.this.loadFolders());
    }

    public void removeListener() {
        listenerRegistration.remove();
    }
}
