package com.example.piston.model;

import androidx.annotation.Nullable;

import java.util.Date;

public class User {

    @Nullable
    private String name;
    @Nullable
    private String surname;
    @Nullable
    private String profilePictureLink;
    @Nullable
    private Post featuredPost;
    @Nullable
    private String phoneNumber;

    private String username;
    private String email;
    private String pwd;
    private Date birthDate;

    public User() {} //Empty constructor for db

    public User (String username, String email, String pwd, Date birthDate){
        this.username = username;
        this.email = email;
        this.birthDate = birthDate;
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Nullable
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

    @Nullable
    public Post getFeaturedPost() {
        return featuredPost;
    }

    public void setFeaturedPost(Post featuredPost) {
        this.featuredPost = featuredPost;
    }

    @Nullable
    public String getFullName(){
        return name + " " + surname;
    }

}

