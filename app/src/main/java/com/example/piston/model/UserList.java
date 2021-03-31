package com.example.piston.model;


import android.util.Patterns;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserList {
    private Map<String, User> users;
    private HashSet<String> emails;

    public UserList (){
        users = new HashMap<>();
        emails = new HashSet<>();
    }

    public boolean isValidPwd(String pwd1, String pwd2){
        return pwd1.equals(pwd2) && pwd1.length()>=7;
    }

    public boolean isValidUsername(String username){
        return users.containsKey(username);
    }

    public boolean isValidEmail(String email){
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches()) && !emails.contains(email);
    }

    public boolean isValidRegister (String username, String pwd1, String pwd2,  String email) throws Exception{
        if (username.trim().isEmpty()){
            throw new Exception("Username required");
        }
        if (email.trim().isEmpty()){
            throw new Exception("Email required");
        }
        if (pwd1.trim().isEmpty()){
            throw new Exception("Password required");
        }
        if (pwd2.trim().isEmpty()){
            throw new Exception("Please, verify you password");
        }
        if (isValidUsername(username)){
            throw new Exception ("Username already taken");
        }
        if (! isValidEmail(email)){
            throw new Exception ("Not a valid email");
        }
        if (! isValidPwd(pwd1,pwd2)){
            throw new Exception("Those passwords didn’t match. Try again.");
        }

        return true;
    }

    public void registerUser(String username, String pwd1, String pwd2,  String email, Date birthDate) throws Exception {
      if (isValidRegister(username,pwd1,pwd2,email)){
               User user = new User (username, email, birthDate, pwd1);
               users.put(username, user);
               emails.add(email);
      }
    }

    public Result<User> login(String username, String password) {
        if (users.containsKey(username)){
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

    public boolean loginUser(String username, String pwd) throws Exception{
        if (users.containsKey(username)){
            if (users.get(username).getPwd().equals(pwd)){
                return true;
            }
            else{
                throw new Exception("Wrong password");
            }
        }
        throw new Exception ("This user does not exist");
    }



}
