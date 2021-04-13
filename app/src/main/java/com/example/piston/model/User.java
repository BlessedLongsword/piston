package com.example.piston.model;

import android.media.Image;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    @Nullable
    private String name;
    @Nullable
    private String surname;
    @Nullable
    private Image icon;
    @Nullable
    private Post featuredPost;
    @Nullable
    private String phoneNumber;

    private String username;
    private String email;
    private String pwd;
    private Date birthDate;


    public User (String username, String email, Date birthDate, String pwd){
        this.username = username;
        this.email = email;
        this.birthDate = birthDate;
        this.pwd = pwd;
    }

    public enum UsernameError {
        NONE, EMPTY, INVALID
    }

    public enum PasswordError {
        NONE, INCORRECT
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
    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
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

