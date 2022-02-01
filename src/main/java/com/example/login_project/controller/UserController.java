package com.example.login_project.controller;

import com.example.login_project.model.User;
import com.example.login_project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private ProjectService projectService;

    public UserController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping("/addUser/{infoString}")
    public void addNewUser(@PathVariable("infoString") String infoString){
        String[] info = infoString.split("&&");
        System.out.println(info[0]+info[1]+info[2]+info[3]);
        projectService.addNewUser(info[0],info[1],info[2],info[3]);
    }

    @GetMapping("/allUsersList")
    public List<User> allUsersList(){
        return projectService.getAllUsers();
    }
}
