package com.example.login_project.controller;
import com.example.login_project.model.Employee;
import com.example.login_project.model.QuestionBank;
import com.example.login_project.service.MainService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class MainController {

    @GetMapping("/")
    public String rootUrl(){
        return "index.html";
    }

    @GetMapping("/RegisterNewEmployee")
    public String register(){
        return "register_employee.html";
    }

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @RequestMapping(value = "/listOfAllEmployees", method = RequestMethod.GET)
    public String getEmployees(Model model) {
        List<Employee> employees = mainService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("employeeCount", employees.size());
        return "all_employees.html";
    }

    @RequestMapping(value = "/listOfAllEmployeesDB", method = RequestMethod.GET)
    public String getEmployeesDB(Model model) throws SQLException {
        List<Employee> employees = mainService.getAllEmployeesDB();
        model.addAttribute("employees", employees);
        model.addAttribute("employeeCount", employees.size());
        return "all_employees.html";
    }

    @PostMapping(value = "/addNewEmployeeToList")
    public String createEmployee(@RequestParam(value = "firstName", required = false) String firstName,
                                @RequestParam(value = "lastName", required = false) String lastName,
                                @RequestParam(value = "gender", required = false) String gender,
                                @RequestParam(value = "email", required = false) String email,
                                @RequestParam(value = "tel", required = false) String tel) {
        if(mainService.checkEmail(email)){
            return "already_registered.html";
        }else {
            mainService.addNewEmployee(firstName, lastName, gender, email, tel);
            return "register_employee.html";
        }
    }

    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam String email) {
        mainService.deleteEmployee(email);
        return "redirect:/listOfAllEmployeesDB";
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public String getQuestions(Model model) throws SQLException {
        List<QuestionBank> questions = mainService.getAllQuestions();
        model.addAttribute("questions", questions);
        model.addAttribute("questionCount", questions.size());
        return "questions.html";
    }

    @RequestMapping(value = "/questionsList", method = RequestMethod.GET)
    public String getQuestionsUpdated(Model model) throws SQLException {
        List<QuestionBank> questions = mainService.getAllQuestionsUpdated();
        if(!questions.isEmpty()){
            model.addAttribute("questions", questions);
            model.addAttribute("questionCount", questions.size());
            return "questions.html";
        } else {
            return "all_questions_answered.html";
        }

    }

    //questionsListSendAnswer
    @PostMapping(value = "/questionsListSendAnswer")
    public String submitAnswer(@RequestParam(value = "id", required = false) int questionID,
                               @RequestParam(value = "ans", required = false) int ans) throws SQLException {
        System.out.println(questionID+": "+ans);
        mainService.sendAnswer(questionID,ans);
        //mainService.sendAnswer(questionID,ans);
        return "redirect:/questionsList";
    }
}

