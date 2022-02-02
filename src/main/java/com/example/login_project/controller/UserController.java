package com.example.login_project.controller;/*
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
*/


import com.example.login_project.model.User;
import com.example.login_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping("/")
    public String rootUrl(){
        return "index.html";
    }

    @GetMapping("/RegisterNewUser")
    public String register(){
        return "register_user.html";
    }

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/listOfAllUsers", method = RequestMethod.GET)
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("userCount", users.size());
        return "all_users.html";
    }

    @PostMapping(value = "/addNewUserToList")
    public String createUser(@RequestParam(value = "firstName", required = false) String firstName,
                             @RequestParam(value = "lastName", required = false) String lastName,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "password", required = false) String password) {
        userService.addNewUser(firstName, lastName, email, password);
        return "register_user.html";
    }
}

