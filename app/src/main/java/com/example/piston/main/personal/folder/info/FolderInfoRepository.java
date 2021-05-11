package com.example.piston.main.personal.folder.info;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FolderInfoRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final FolderInfoRepository.IFolderInfo listener;

    public interface IFolderInfo {
        void setTitle(String title);
        void setDescription(String description);
    }

    public FolderInfoRepository(FolderInfoRepository.IFolderInfo listener, String folderID) {
        this.listener = listener;
        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        db.collection("users")
                .document(user)
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
