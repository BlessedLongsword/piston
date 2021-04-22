package com.example.piston.main.groups.createGroup;

import android.util.Log;

import com.example.piston.data.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateGroupRepository {

    private final CreateGroupRepository.ICreateGroup listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public interface ICreateGroup {
        void setGroupID(String groupID);
        void setTitleStatus(CreateGroupResult.TitleError error);
        void setCreateError();
        void setCreateFinished();
        void setLoadingFinished();
    }

    public CreateGroupRepository(CreateGroupRepository.ICreateGroup listener) {
        this.listener = listener;
    }

    public void generateGroupID() {
        Map data = new HashMap();
        data.put("Null", null);
        db.collection("groups")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                   listener.setGroupID(documentReference.getId());
                   Log.d("DBReadTAG", "The id is: " + documentReference.getId());
                   db.collection("groups").document(documentReference.getId())
                           .delete().addOnCompleteListener(task -> {
                               listener.setLoadingFinished();
                   });
                });
    }

    public void checkTitle(String title) {
        if (title.trim().equals(""))
            listener.setTitleStatus(CreateGroupResult.TitleError.EMPTY);
        else
            listener.setTitleStatus(CreateGroupResult.TitleError.NONE);
    }

    public void createGroup(String title, String description, String groupID) {
        if (title.trim().equals("")) {
            listener.setLoadingFinished();
            listener.setCreateError();
        } else {
            Group group = new Group(title, description, groupID);
            db.collection("groups").document(groupID).set(group).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    listener.setLoadingFinished();
                    listener.setCreateFinished();
                }
            });
            Map<String, String> data = new HashMap<>();
            data.put("id", groupID);
            db.collection("users").document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                    .collection("groups").document(groupID).set(data);
        }
    }

}
