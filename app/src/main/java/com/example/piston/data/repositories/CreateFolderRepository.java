package com.example.piston.data.repositories;

import com.example.piston.data.CreateFolderResult;
import com.example.piston.data.Folder;
import com.example.piston.data.Section;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class CreateFolderRepository {

    private final CreateFolderRepository.ICreateFolder listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public interface ICreateFolder {
        void setTitleStatus(CreateFolderResult.TitleError titleError);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
    }

    public CreateFolderRepository(CreateFolderRepository.ICreateFolder listener) {
        this.listener = listener;
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreateFolderResult.TitleError.EMPTY);
        else {
            DocumentReference docRef = db.collection("categories").document(title);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds.exists())
                        listener.setTitleStatus(CreateFolderResult.TitleError.EXISTS);
                    else
                        listener.setTitleStatus(CreateFolderResult.TitleError.NONE);
                }
            });
        }
    }

    public void createFolder(String title, String description) {
        String user = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail());
        if (title.trim().equals("")) {
            listener.setTitleStatus(CreateFolderResult.TitleError.EMPTY);
            listener.setCreateError();
            listener.setLoadingFinished();
        } else {
            DocumentReference docRef = db.collection("users")
                    .document(user)
                    .collection("folders").document(title);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds.exists()) {
                        listener.setTitleStatus(CreateFolderResult.TitleError.EXISTS);
                        listener.setCreateError();
                        listener.setLoadingFinished();
                    } else {
                        Folder folder = new Folder(title, description);
                        db.collection("users")
                                .document(user)
                                .collection("folders")
                                .document(title).set(folder);
                        listener.setCreateFinished();
                        listener.setLoadingFinished();
                    }
                }
            });
        }
    }
}
