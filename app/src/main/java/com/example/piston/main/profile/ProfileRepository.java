package com.example.piston.main.profile;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.piston.authentication.register.RegisterResult;
import com.example.piston.data.posts.Post;
import com.example.piston.data.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileRepository {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final IProfile listener;

    private final String email;

    public interface IProfile {
        void setUserNameField(String username);
        void setEmailField(String email);
        void setFullNameField(String fullName);
        void setBirthDateField(Date birthDate);
        void setPhoneNumberField(String phoneNumber);
        void setImageLink(String imageLink);
        void setBirthDateStatus(RegisterResult.BirthDateError birthDateError);
        void setLoadingFinished();
        void setIsCurrentUser(boolean isCurrentUser);
        void setFeaturedPost(Post featuredPost);
    }

    public ProfileRepository(IProfile listener, String email) {
        this.listener = listener;
        this.email = (email == null) ? Objects.requireNonNull(mAuth.getCurrentUser()).getEmail() : email;
        listener.setIsCurrentUser(Objects.equals(this.email,
                Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));
        listenChanges();
    }


    public void loadProfile(){
        db.collection("users")
                .document(Objects.requireNonNull(email))
                .get().addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    listener.setUserNameField(Objects.requireNonNull(user).getUsername());
                    listener.setEmailField(user.getEmail());
                    listener.setFullNameField(user.getName());
                    listener.setBirthDateField(user.getBirthDate());
                    listener.setPhoneNumberField(user.getPhoneNumber());
                    listener.setImageLink(user.getProfilePictureLink());
                    loadFeaturedPost();
        });
    }

    public void loadFeaturedPost() {
        db.collection("categories").get().addOnCompleteListener(task -> {
            for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                db.collection("categories").document(ds.getId())
                        .collection("posts")
                        .whereEqualTo("ownerEmail", email)
                        .orderBy("numLikes", Query.Direction.DESCENDING)
                        .get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                try {
                                    DocumentSnapshot featuredPostDocumentSnapshot =
                                            task1.getResult().getDocuments().get(0);
                                    listener.setFeaturedPost(featuredPostDocumentSnapshot
                                            .toObject(Post.class));
                                } catch (NullPointerException | IndexOutOfBoundsException e) {
                                    listener.setFeaturedPost(null);
                                }
                            } else {
                                Log.d("DBReadTAG", Objects.requireNonNull(task1.getException()).getMessage());
                            }
                });
            }
        });
    }

    public void checkBirthDate(String birthDate) {
        Pattern pattern = Pattern.compile("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)");
        Matcher matcher = pattern.matcher(birthDate);
        if (matcher.matches())
            listener.setBirthDateStatus(RegisterResult.BirthDateError.NONE);
        else
            listener.setBirthDateStatus(RegisterResult.BirthDateError.INVALID);
    }

    public void editName(String text) {
        editField("name", text);
    }

    public void editPhone(String text) {
        editField("phoneNumber", text);
    }

    @SuppressLint("SimpleDateFormat")
    public void editBirth(String text) {
        try {
            editField("birthDate", new SimpleDateFormat("dd/MM/yyyy").parse(text));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void editField(String field, Object data) {
        String currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        DocumentReference docRef = db.collection("users")
                .document(Objects.requireNonNull(currentUser));

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            docRef.update(field, data);
            listener.setLoadingFinished();
        });
    }

    private void listenChanges() {
        db.collection("users")
                .document(email)
                .addSnapshotListener((value, error) -> ProfileRepository.this.loadProfile());
    }

}
