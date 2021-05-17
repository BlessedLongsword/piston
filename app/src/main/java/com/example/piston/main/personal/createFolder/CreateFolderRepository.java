package com.example.piston.main.personal.createFolder;

import com.example.piston.data.sections.Folder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class CreateFolderRepository {

    private final CreateFolderRepository.ICreateFolder listener;
    private final CollectionReference folderColRef;


    public interface ICreateFolder {
        void setTitleStatus(CreateFolderResult.TitleError titleError);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
    }

    public CreateFolderRepository(CreateFolderRepository.ICreateFolder listener) {
        this.listener = listener;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.folderColRef = db.collection("users").document(user)
                .collection("folders");
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreateFolderResult.TitleError.EMPTY);
        else {
            folderColRef.document(title).get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    DocumentSnapshot ds = task.getResult();
                    if (Objects.requireNonNull(ds).exists())
                        listener.setTitleStatus(CreateFolderResult.TitleError.EXISTS);
                    else
                        listener.setTitleStatus(CreateFolderResult.TitleError.NONE);
                }
            });
        }
    }

    public void createFolder(String title, String description) {
        if (title.trim().equals("")) {
            listener.setTitleStatus(CreateFolderResult.TitleError.EMPTY);
            listener.setCreateError();
            listener.setLoadingFinished();
        } else {
            String id = folderColRef.document().getId();

            DocumentReference docRef = folderColRef.document(id);

            docRef.get().addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    DocumentSnapshot ds = task.getResult();
                    if (Objects.requireNonNull(ds).exists()) {
                        listener.setTitleStatus(CreateFolderResult.TitleError.EXISTS);
                        listener.setCreateError();
                    } else {
                        Folder folder = new Folder(id, title, description);
                        docRef.set(folder);
                        listener.setCreateFinished();
                    }
                    listener.setLoadingFinished();
                }
            });
        }
    }
}
