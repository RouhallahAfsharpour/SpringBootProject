package com.example.login_project.controller;

import com.example.login_project.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Controller
public class ProjectController {

    RestTemplate restTemplate;
    @GetMapping("/")
    public String rootUrl(){
        return "index.html";
    }

    @GetMapping("/RegisterNewUser")
    public String registerUser(){
        return "register_user.html";
    }

    @RequestMapping(value = "/addNewUserToList", method = RequestMethod.GET)
    public String getListOfUsers(@RequestParam(value = "firstNameUser", required = false) String firstName, HttpServletRequest request, Model model) {

        restTemplate = new RestTemplate();

        String itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/addUser/" + firstName;

        List<User> response = restTemplate.getForObject(
                itemResourceUrl,
                List.class
        );

        return "register_user.html";
    }

    @GetMapping("/listOfAllUsers")
    public String getListOfAllUsers(HttpServletRequest request, Model model) {

        restTemplate = new RestTemplate();
        String userResourceUrl = "http://localhost:" + request.getLocalPort() + "/allUsersList";

        List<User> responseUsers = restTemplate.getForObject(
                userResourceUrl,
                List.class
        );

        model.addAttribute("users", responseUsers);
        model.addAttribute("userCount", responseUsers.size());
        return "all_users.html";
    }
}
