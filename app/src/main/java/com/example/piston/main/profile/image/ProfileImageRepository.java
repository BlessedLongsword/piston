package com.example.piston.main.profile.image;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class ProfileImageRepository {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final IProfileImage listener;
    private String username;
    private final DocumentReference userDocRef;

    public interface IProfileImage {
        void setImageLink(String imageLink);
    }

    ProfileImageRepository(IProfileImage listener, String email) {
        this.listener = listener;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email1 = (email == null) ? Objects.requireNonNull(mAuth.getCurrentUser()).getEmail() : email;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("users").document(Objects.requireNonNull(email1));

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                username = (String) task.getResult().get("username");
        });
        loadImage();
    }

    public void loadImage() {
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.setImageLink((String) task.getResult().get("profilePictureLink"));
            }
        });
    }

    public void setImage(Uri image) {
        StorageReference storageRef = storage.getReference();
        String randomId = UUID.randomUUID().toString();
        String path = "users/" + username + "/profile";
        String imageId = path + "/" + randomId;
        StorageReference imageRef = storageRef.child(imageId);
        UploadTask uploadTask = imageRef.putFile(image);

        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                   userDocRef.update("profilePictureLink", uri.toString());
                   loadImage();
                })));
    }

}
