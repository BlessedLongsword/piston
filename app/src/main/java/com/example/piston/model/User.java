package com.example.piston.model;

import android.media.Image;

import java.util.Date;

public class User {
    private String username;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String pwd;
    private Image icon;
    private Date birthDate;
    private Post featuredPost;

    public User (String username, String email, Date birthDate, String pwd){
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Post getFeaturedPost() {
        return featuredPost;
    }

    public void setFeaturedPost(Post featuredPost) {
        this.featuredPost = featuredPost;
    }

    public String getFullName(){
        return name + " " + surname;
    }

}

