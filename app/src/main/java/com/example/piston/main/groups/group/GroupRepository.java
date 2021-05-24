package com.example.piston.main.groups.group;

import android.util.Log;

import com.example.piston.data.posts.Post;
import com.example.piston.utilities.Values;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

public class GroupRepository {

    public interface IGroup {
        void setGroupPosts(ArrayList<Post> posts);
        void setTitle(String title);
        void setFromShareJoinedGroup();
        void setFilter(String filter);
        void setPermissions(boolean modMode, Integer priority);
    }

    private final GroupRepository.IGroup listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistrationPosts;
    private ListenerRegistration listenerRegistrationGroup;
    private final DocumentReference groupDocRef;
    private Query postsQuery;
    private final String email;

    public GroupRepository(GroupRepository.IGroup listener, String group) {
        this.listener = listener;
        Log.d("what",group);
        this.groupDocRef = db.collection("groups").document(group);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        postsQuery = groupDocRef.collection("posts").orderBy("pinned", Query.Direction.DESCENDING)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        listenChanges();
    }

    private void loadGroupPosts() {
        postsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Post> posts = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(
                        task.getResult())) {
                    Post post = documentSnapshot.toObject(Post.class);
                    Timestamp timestamp = (Timestamp) documentSnapshot.get("timestamp", ESTIMATE);
                    post.setTimeAgo(Objects.requireNonNull(timestamp).getSeconds());
                    posts.add(post);
                }
                listener.setGroupPosts(posts);
            }
        });
    }

    private void loadGroupParams() {
        groupDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                listener.setTitle(Objects.requireNonNull(task.getResult().get("title")).toString());
                groupDocRef.collection("members").document(email).get()
                        .addOnCompleteListener(task1 -> {
                            if (task.isSuccessful()) {
                                Object priority = task1.getResult().get("priority");
                                if (priority != null) {
                                    long priority1 = (long) priority;
                                    listener.setPermissions((boolean) Objects.requireNonNull
                                            (task.getResult().get("modMode")), (int) priority1);
                                }
                            }
                        });
            }
        });
    }

    public void updateQuery(String field, boolean descending) {
        if (field.equals(Values.FILTER_DEFAULT))
            postsQuery = groupDocRef.collection("posts").orderBy("pinned",
                    Query.Direction.DESCENDING).orderBy("timestamp", Query.Direction.DESCENDING);
        else
            postsQuery = groupDocRef.collection("posts").orderBy("pinned",
                    Query.Direction.DESCENDING).orderBy(field, (descending) ?
                    Query.Direction.DESCENDING : Query.Direction.ASCENDING)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
        loadGroupPosts();
        listener.setFilter(field);
    }

    private void listenChanges() {
        listenerRegistrationPosts = postsQuery.addSnapshotListener((snapshots, e) -> GroupRepository
                .this.loadGroupPosts());
        listenerRegistrationGroup = groupDocRef.addSnapshotListener((value, error) -> GroupRepository
                .this.loadGroupParams());
    }

    public void fromShareJoinGroup(String groupID) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String user = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            DocumentReference docRef = db.collection("groups").document(groupID);
            docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        long numMembers = (long) Objects.requireNonNull(task.getResult().get("numMembers"));
                        docRef.update("numMembers", ++numMembers);
                        DocumentReference docRef2 = db.collection("users")
                                .document(Objects.requireNonNull(user))
                                .collection("groups")
                                .document(groupID);
                        long finalNumMembers = numMembers;
                        docRef2.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("id", groupID);
                                data.put("title", task.getResult().get("title"));
                                data.put("timestamp", FieldValue.serverTimestamp());
                                data.put("numMembers", finalNumMembers);
                                docRef2.set(data);
                                data.clear();
                                data.put("id", user);
                                data.put("priority", 2);
                                db.collection("groups")
                                        .document(groupID)
                                        .collection("members")
                                        .document(user)
                                        .set(data).addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                listener.setFromShareJoinedGroup();
                                            }
                                });
                            }
                        });
                    }
            });
    }

    public void removeListener() {
        listenerRegistrationGroup.remove();
        listenerRegistrationPosts.remove();
    }
}