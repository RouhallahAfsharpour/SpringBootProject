package com.example.login_project.service;

import com.example.login_project.model.User;
import com.example.login_project.repository.MainRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    public static void addNewUser(String firstName, String lastName, String email, String tel, String gender){
        MainRepository.addUser(firstName, lastName, email, tel, gender);
    }

    public static List<User> getAllUsers(){
        return MainRepository.getAllUsers();
    }

    public static List<User> getAllUsersDB() throws SQLException {
        return MainRepository.getAllUsersDB();
    }
}
