package com.example.piston.main.groups.joinGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JoinGroupRepository {

    private final JoinGroupRepository.IJoinGroup listener;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String user;

    public interface IJoinGroup {
        void setGroupCodeError(JoinGroupResult.JoinError error);
        void setLoadingFinished();
        void setJoinGroupFinished();
    }

    public JoinGroupRepository(JoinGroupRepository.IJoinGroup listener) {
        this.listener = listener;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
    }

    public void checkGroupCode(String groupCode) {
        if (groupCode.trim().equals(""))
            listener.setGroupCodeError(JoinGroupResult.JoinError.EMPTY);
        else
            listener.setGroupCodeError(JoinGroupResult.JoinError.NONE);
    }

    public void joinGroup(String groupCode) {
        if (groupCode.trim().equals("")) {
            listener.setGroupCodeError(JoinGroupResult.JoinError.EMPTY);
            listener.setLoadingFinished();
        }
        else {
            DocumentReference docRef = db.collection("groups").document(groupCode);
            docRef.get().addOnCompleteListener(task -> {
                DocumentSnapshot ds = task.getResult();
                if (Objects.requireNonNull(ds).exists()) {
                    DocumentReference docRef2 = db.collection("users")
                                                    .document(user)
                                                    .collection("groups")
                                                    .document(groupCode);
                    docRef2.get().addOnCompleteListener(task2 -> {
                       DocumentSnapshot ds2 = task2.getResult();
                       if (Objects.requireNonNull(ds2).exists()) {
                           listener.setLoadingFinished();
                           listener.setGroupCodeError(JoinGroupResult.JoinError.ALREADY_JOINED);
                       }
                       else {
                           Map<String, String> data = new HashMap<>();
                           data.put("id", groupCode);
                           docRef2.set(data).addOnCompleteListener(task3 -> {
                               if (task3.isSuccessful()) {
                                   listener.setLoadingFinished();
                                   listener.setJoinGroupFinished();
                               }
                           });
                           data.clear();
                           data.put("id", user);
                           db.collection("groups")
                                   .document(groupCode)
                                   .collection("members")
                                   .document(user)
                                   .set(data);
                       }
                    });
                }
                else {
                    listener.setLoadingFinished();
                    listener.setGroupCodeError(JoinGroupResult.JoinError.NOT_EXISTS);
                }
            });
        }
    }

}
