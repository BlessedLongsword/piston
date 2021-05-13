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
    private final DocumentReference docRef;

    public FolderInfoRepository(FolderInfoRepository.IFolderInfo listener, String folderID) {
        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        docRef = db.collection("users")
                .document(Objects.requireNonNull(user))
                .collection("folders")
                .document(folderID);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setTitle((String) Objects.requireNonNull(task.getResult()).get("title"));
                listener.setDescription((String) task.getResult().get("description"));
            }
        });
    }

    public void deleteFolder() {
        docRef.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                // Delete subcollection
                for (QueryDocumentSnapshot snapshot : Objects.requireNonNull(
                        task.getResult())) {
                    DocumentReference docRef1 = docRef.collection("posts")
                            .document(snapshot.getId());

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
                    docRef1.delete();
                }
            }
        });
        docRef.delete();
    }

}
