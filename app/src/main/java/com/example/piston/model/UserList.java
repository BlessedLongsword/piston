package com.example.piston.model;


import android.util.Log;
import android.util.Patterns;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserList {
    private Map<String, User> users;
    private HashSet<String> emails;

    public UserList (){
        users = new HashMap<>();
        users.put("admin", new User("admin", "admin@gmail.com", new Date(System.currentTimeMillis()), "admin"));
        emails = new HashSet<>();
        emails.add("admin@gmail.com");
        Log.d("userList", " Creating hash");
    }
    public String getUserUsername (String username){
        return username;
    }

    public String getUserName (String username){
        return users.get(username).getFullName();
    }

    public String getUserPhone (String username){
        return users.get(username).getPhoneNumber();
    }

    public boolean isValidPwd(String pwd1, String pwd2){
        return pwd1.equals(pwd2) && pwd1.length() >= 6;
    }

    public boolean isValidUsername(String username){
        return users.containsKey(username);
    }

    public boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean emailExists(String email){
        return emails.contains(email);
    }

    public Result<String> register(String username, String pwd1, String pwd2,  String email, Date birthDate) {
        if (username.trim().isEmpty()){
            return new Result.Error(new IOException("Username required"));
        }
        if (pwd1.trim().isEmpty()){
            return new Result.Error(new IOException("Password required"));
        }
        if (isValidUsername(username)){
            return new Result.Error(new IOException("This username is already taken"));
        }
        if (! isValidEmail(email)){
            return new Result.Error(new IOException("Not a valid email"));
        }
        if ( emailExists(email)){
            return new Result.Error(new IOException("This email is already taken"));
        }
        if (pwd1.length() < 6){
            return new Result.Error(new IOException("Password must be at least 6 characters"));
        }
        if (! isValidPwd(pwd1,pwd2)){
            return new Result.Error(new IOException("Passwords do not match"));
        }
        User user = new User(username, email, birthDate, pwd1);
        addUser(user);
        return new Result.Success<>("Registration Successful");
    }

    public void addUser(User user){
        users.put(user.getUsername(), user);
        emails.add(user.getEmail());
    }

    public Result<User> login(String username, String password) {
        Log.d("what", username + " not added?? " + users);
        if (isValidUsername(username)){
            if (users.get(username).getPwd().equals(password)) {
                return new Result.Success<>(users.get(username));
            }
            else {
                return new Result.Error(new IOException("Wrong password"));
            }
        }
        else {
            return new Result.Error(new IOException("This user does not exist"));
        }
    }
}
