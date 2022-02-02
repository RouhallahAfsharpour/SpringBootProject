package com.example.login_project.service;

import com.example.login_project.model.User;
import com.example.login_project.repository.MainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public static void addNewUser(String firstName, String lastName, String email, String tel){
        MainRepository.addUser(firstName, lastName, email, tel);
    }

    public static List<User> getAllUsers(){
        return MainRepository.getAllUsers();
    }
}
