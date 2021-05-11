package com.example.piston.data;


import java.util.Date;

public class User {

    private String name;
    private String profilePictureLink;
    private Post featuredPost;
    private String phoneNumber;

    private String username;
    private String email;
    private Date birthDate;

    public User() {}

    public User(String username, String email, Date birthDate) {
        this.username = username;
        this.email = email;
        this.birthDate = birthDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePictureLink() {
        return profilePictureLink;
    }

    public void setProfilePictureLink(String profilePictureLink) {
        this.profilePictureLink = profilePictureLink;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Post getFeaturedPost() {
        return featuredPost;
    }

    public void setFeaturedPost(Post featuredPost) {
        this.featuredPost = featuredPost;
    }

}
