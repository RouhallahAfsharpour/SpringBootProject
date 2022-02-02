package com.example.login_project.service;

import com.example.login_project.model.User;
import com.example.login_project.repository.MainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public static void addNewUser(String firstName, String lastName, String email, String password){
        MainRepository.addUser(firstName, lastName, email, password);
    }

    public static List<User> getAllUsers(){
        return MainRepository.getAllUsers();
    }
}
