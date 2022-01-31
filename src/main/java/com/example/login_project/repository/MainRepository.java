package com.example.login_project.repository;

import com.example.login_project.model.User;

import java.util.ArrayList;
import java.util.List;


public class MainRepository {
    public static List<User> allUsers = new ArrayList<User>();

    public static void addUser(String firstName, String lastName, String email, String password){
        allUsers.add(new User(firstName, lastName, email, password));
        System.out.println(allUsers.size());
        System.out.println(firstName);
    }

    public static List<User> getAllUsers(){
        List<User> users = allUsers;
        return users;
    }
}
