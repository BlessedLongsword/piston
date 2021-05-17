package com.example.piston.data.users;

import java.util.Date;

public class User {

    private String username, email;
    private Date birthDate;

    private String name;
    private String phoneNumber;
    private String profilePictureLink;

    public User() {}

    public User(String username, String email, Date birthDate) {
        setUsername(username);
        setEmail(email);
        setBirthDate(birthDate);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @SuppressWarnings("unused")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePictureLink() {
        return profilePictureLink;
    }

    @SuppressWarnings("unused")
    public void setProfilePictureLink(String profilePictureLink) {
        this.profilePictureLink = profilePictureLink;
    }
}
