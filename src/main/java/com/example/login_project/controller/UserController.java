package com.example.login_project.controller;
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
                             @RequestParam(value = "tel", required = false) String tel,
                             @RequestParam(value = "gender", required = false) String gender) {
        userService.addNewUser(firstName, lastName, email, tel, gender);
        return "register_user.html";
    }
}

