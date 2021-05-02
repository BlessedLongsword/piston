package com.example.piston.main.groups.group.info;

import com.google.firebase.firestore.FirebaseFirestore;

public class GroupInfoRepository {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    final GroupInfoRepository.IGroupInfo listener;

    public interface IGroupInfo {
        void setTitle(String title);
        void setDescription(String description);
        void setImageLink(String imageLink);
    }

    public GroupInfoRepository(GroupInfoRepository.IGroupInfo listener, String groupID) {
        this.listener = listener;
        db.collection("groups")
                .document(groupID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setTitle((String) task.getResult().get("title"));
                listener.setDescription((String) task.getResult().get("description"));
                listener.setImageLink((String) task.getResult().get("imageLink"));
            }
        });
    }

}
