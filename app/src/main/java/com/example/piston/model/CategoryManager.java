package com.example.piston.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryManager {

    private final Map<String, Section> folders;

    public CategoryManager() {
        folders = new HashMap<>();
    }

    public void createFolder(String title, String description) {
        folders.put(title, new Section(title, description));
    }

    public Section getFolder(String title) {
        return folders.get(title);
    }

    public ArrayList<String> getFolderNames() {
        return new ArrayList<String>(folders.keySet());
    }
}
