package com.example.piston.main.personal.folder.info;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FolderInfoRepository {

    public interface IFolderInfo {
        void setTitle(String title);
        void setDescription(String description);
    }

    public FolderInfoRepository(FolderInfoRepository.IFolderInfo listener, String folderID) {
        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
}
