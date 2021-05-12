package com.example.piston.main.personal.folder.info;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class FolderInfoRepository {

    public interface IFolderInfo {
        void setTitle(String title);
        void setDescription(String description);
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String folderID, user;

    public FolderInfoRepository(FolderInfoRepository.IFolderInfo listener, String folderID) {
        this.folderID = folderID;
        user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        db.collection("users")
                .document(Objects.requireNonNull(user))
                .collection("folders")
                .document(folderID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setTitle((String) Objects.requireNonNull(task.getResult()).get("title"));
                listener.setDescription((String) task.getResult().get("description"));
            }
        });
    }

    public void deleteFolder() {
        DocumentReference docRef = db.collection("users")
                .document(user)
                .collection("folders")
                .document(folderID);

        docRef.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                // Delete subcollection
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    docRef.collection("posts")
                            .document(documentSnapshot.getId())
                            .delete();
                }
            }
            docRef.delete();
        });
    }
}
