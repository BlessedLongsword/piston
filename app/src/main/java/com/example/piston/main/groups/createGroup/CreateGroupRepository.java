package com.example.piston.main.groups.createGroup;

import android.net.Uri;

import com.example.piston.data.sections.Group;
import com.example.piston.data.users.GroupMember;
import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        listener.setGroupID(db.collection(Values.GROUPS).document().getId());
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreateGroupResult.TitleError.EMPTY);
        else
            listener.setTitleStatus(CreateGroupResult.TitleError.NONE);
    }

    public void createGroup(String title, String description, String groupID,
                            Uri image, boolean connected) {

        if (title.trim().equals("")) {
            listener.setLoadingFinished();
            listener.setCreateError();
        }
        else if (!connected) {
            listener.setLoadingFinished();
            listener.setErrorMessage("Need internet to create group");
        }
        else if (image == null) {
            listener.setLoadingFinished();
            listener.setErrorMessage("Group must have an image");
        }
        else {
            StorageReference imageRef = storage.getReference().child(Values.GROUPS)
                    .child(groupID).child("groupImage");
            UploadTask uploadTask = imageRef.putFile(image);

            uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        String imageLink = uri.toString();

                        Group group = new Group(groupID, title, description, imageLink);

                        db.collection(Values.GROUPS).document(groupID).set(group)
                                .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                listener.setLoadingFinished();
                                listener.setCreateFinished();
                            }
                        });
                        Map<String, Object> data = new HashMap<>();
                        data.put("id", groupID);
                        DocumentReference userGroupDocRef = db.collection("users")
                                                                .document(user)
                                                                .collection("groups")
                                                                .document(groupID);
                        userGroupDocRef.set(data);
                        userGroupDocRef.update("timestamp", FieldValue.serverTimestamp());
                        data.clear();
                        data.put("id", user);
                        data.put("priority", GroupMember.OWNER);
                        DocumentReference groupDocRef = db.collection("groups").document(groupID);
                        groupDocRef.collection("members").document(user).set(data);
                    }));
        }
    }
}
