package com.matelau.junior.centsproject.Models.UserModels;

/**
 * POJO for registering a User
 */
public class User {
     String first_name;
     String last_name;
     String email;
     String password;
     String password_confirmation;
     String email_type;

    public User(String n, String l, String e, String p, String p1, String type){
        first_name = n;
        last_name = l;
        email = e;
        password = p;
        password_confirmation = p1;
        email_type = type;

    }
}
