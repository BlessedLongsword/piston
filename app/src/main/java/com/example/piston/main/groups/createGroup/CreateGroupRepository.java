package com.example.piston.main.groups.createGroup;

import com.example.piston.data.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CreateGroupRepository {

    private final CreateGroupRepository.ICreateGroup listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final String user;

    public interface ICreateGroup {
        void setGroupID(String groupID);
        void setTitleStatus(CreateGroupResult.TitleError error);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
        void setErrorMessage(String message);
    }

    public CreateGroupRepository(CreateGroupRepository.ICreateGroup listener) {
        this.listener = listener;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
    }

    public void generateGroupID() {
        listener.setGroupID(db.collection("groups").document().getId());
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreateGroupResult.TitleError.EMPTY);
        else
            listener.setTitleStatus(CreateGroupResult.TitleError.NONE);
    }

    public void createGroup(String title, String description, String groupID, byte[] image, boolean connected) {
        if (title.trim().equals("")) {
            listener.setLoadingFinished();
            listener.setCreateError();
        }
        else if (!connected) {
            listener.setErrorMessage("Need internet to create group");
        }
        else if (image == null)
            listener.setErrorMessage("Group must have an image");
        else {
            StorageReference storageRef = storage.getReference();
            String randomId = UUID.randomUUID().toString();
            String path = "groups/" + groupID;
            String imageId = path + "/" + randomId;
            StorageReference imageRef = storageRef.child(imageId); //Falta comprovar que sigui nou?
            UploadTask uploadTask = imageRef.putBytes(image);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
            }).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        String imageLink = uri.toString();

                        Group group = new Group(title, description, groupID, imageId, imageLink);

                        db.collection("groups")
                                .document(groupID)
                                .set(group)
                                .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                listener.setLoadingFinished();
                                listener.setCreateFinished();
                            }
                        });
                        Map<String, String> data = new HashMap<>();
                        data.put("id", groupID);
                        db.collection("users")
                                .document(user)
                                .collection("groups")
                                .document(groupID)
                                .set(data);

                        data.clear();
                        data.put("id", user);
                        db.collection("groups")
                                .document(groupID)
                                .collection("mods")
                                .document(user)
                                .set(data);
                    }));
        }
    }
}
