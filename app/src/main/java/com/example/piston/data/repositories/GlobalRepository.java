package com.example.piston.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

public class GlobalRepository {

    private final IGlobal listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface IGlobal {

    }

    public GlobalRepository(IGlobal listener) {
        this.listener = listener;
    }


    public void loadCategories() {

    }

}
