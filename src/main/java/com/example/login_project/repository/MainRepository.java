package com.example.login_project.repository;
import com.example.login_project.model.User;
import java.util.ArrayList;
import java.util.List;

public class MainRepository {
    public static List<User> allUsers = new ArrayList<User>();

    public static void addUser(String firstName, String lastName, String email, String tel, String gender){
        allUsers.add(new User(firstName, lastName, email, tel, gender));
    }

    public static List<User> getAllUsers(){
        List<User> users = allUsers;
        return users;
    }
}
