package com.example.piston.main.personal.folder.info;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class FolderInfoRepository {

    public interface IFolderInfo {
        void setTitle(String title);
        void setFinished();
        void setDescription(String description);
    }

    private final DocumentReference folderDocRef;
    private final FolderInfoRepository.IFolderInfo listener;

    public FolderInfoRepository(FolderInfoRepository.IFolderInfo listener, String folderID) {
        this.listener = listener;
        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        folderDocRef = db.collection("users")
                .document(Objects.requireNonNull(user))
                .collection("folders")
                .document(folderID);
        
        updateParams();
    }

    public void deleteFolder() {
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("users").child(Objects.requireNonNull(email)).child(folderDocRef.getId());

        folderDocRef.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                // Delete sub collection
                for (QueryDocumentSnapshot snapshot : Objects.requireNonNull(
                        task.getResult())) {
                    String id = snapshot.getId();
                    DocumentReference docRef1 = folderDocRef.collection("posts")
                            .document(id);

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
                    if (snapshot.get("imageLink") != null)
                        storageReference.child(id).delete();
                    docRef1.delete();
                }
            }
        });
        folderDocRef.delete();
    }

    public void updateParams() {
        folderDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setTitle((String) Objects.requireNonNull(task.getResult()).get("title"));
                listener.setDescription((String) task.getResult().get("description"));
            }
        });
    }

    public void editTitle(String text) {
        folderDocRef.get().addOnSuccessListener(documentSnapshot -> {
            folderDocRef.update("title", text);
            listener.setFinished();
        });
    }

    public void editDescription(String text) {
        folderDocRef.get().addOnSuccessListener(documentSnapshot -> {
            folderDocRef.update("description", text);
            listener.setFinished();
        });
    }
}
