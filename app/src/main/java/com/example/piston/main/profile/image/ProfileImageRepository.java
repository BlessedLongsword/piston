package com.example.piston.main.profile.image;

import android.net.Uri;

import com.example.piston.utilities.Values;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class ProfileImageRepository {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final IProfileImage listener;
    private final String email;
    private final DocumentReference userDocRef;

    public interface IProfileImage {
        void setImageLink(String imageLink);
    }

    ProfileImageRepository(IProfileImage listener, String email2) {
        this.listener = listener;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        email = (email2 == null) ? Objects.requireNonNull(mAuth.getCurrentUser()).getEmail() : email2;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("users").document(Objects.requireNonNull(email));

        loadImage();
    }

    public void loadImage() {
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String profilePictureLink = (String) task.getResult().get("profilePictureLink");
                listener.setImageLink(profilePictureLink);
                updatePostsProfilePicture(profilePictureLink);
            }
        });
    }

    public void setImage(Uri image) {
        StorageReference imageRef = storage.getReference().child("users").child(email)
                .child("profilePicture");
        UploadTask uploadTask = imageRef.putFile(image);

        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                   userDocRef.update("profilePictureLink", uri.toString());
                   loadImage();
                })));
    }

    public void deleteImage() {
        StorageReference profileImageReference = storage.getReference().child("users").child(email)
                .child("profilePicture");
        profileImageReference.delete();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users")
                .document(email);
        docRef.get().addOnSuccessListener(documentSnapshot -> docRef.update("profilePictureLink", null));
        listener.setImageLink(null);
    }

    private void updatePostsProfilePicture(String profileImageLink) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference groupsCol = db.collection(Values.GROUPS);
        CollectionReference categoriesCol = db.collection(Values.GLOBAL);
        groupsCol.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot ds : task.getResult()) {
                CollectionReference groupPosts = groupsCol.document(ds.getId()).collection("posts");
                groupPosts.get().addOnCompleteListener(task1 -> {
                    for (DocumentSnapshot ps : task1.getResult()) {
                        if (Objects.equals(ps.get("ownerEmail"), email))
                            ps.getReference().update("profileImageLink", profileImageLink);
                        groupPosts.document(ps.getId()).collection("replies")
                                .whereEqualTo("ownerEmail", email).get().addOnCompleteListener(task2 -> {
                                    for (DocumentSnapshot rs : task2.getResult())
                                        rs.getReference().update("ownerImageLink", profileImageLink);
                        });
                    }
                });
            }
        });
        categoriesCol.get().addOnCompleteListener(task -> {
            for (DocumentSnapshot ds : task.getResult()) {
                CollectionReference collectionPosts = categoriesCol.document(ds.getId())
                        .collection("posts");
                collectionPosts.get().addOnCompleteListener(task1 -> {
                    for (DocumentSnapshot ps : task1.getResult()) {
                        if (Objects.equals(ps.get("ownerEmail"), email))
                            ps.getReference().update("profileImageLink", profileImageLink);
                        collectionPosts.document(ps.getId()).collection("replies")
                                .whereEqualTo("ownerEmail", email).get().addOnCompleteListener(task2 -> {
                            for (DocumentSnapshot rs : task2.getResult())
                                rs.getReference().update("ownerImageLink", profileImageLink);
                        });
                    }
                });
            }
        });
    }

}
