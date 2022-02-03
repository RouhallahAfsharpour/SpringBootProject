package com.example.login_project.controller;
import com.example.login_project.model.Employee;
import com.example.login_project.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/")
public class EmployeeController {

    @GetMapping("/")
    public String rootUrl(){
        return "index.html";
    }

    @GetMapping("/RegisterNewEmployee")
    public String register(){
        return "register_employee.html";
    }

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(value = "/listOfAllEmployees", method = RequestMethod.GET)
    public String getEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("employeeCount", employees.size());
        return "all_employees.html";
    }

    @RequestMapping(value = "/listOfAllEmployeesDB", method = RequestMethod.GET)
    public String getEmployeesDB(Model model) throws SQLException {
        List<Employee> employees = employeeService.getAllEmployeesDB();
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
        employeeService.addNewEmployee(firstName, lastName, gender, email, tel);
        return "register_employee.html";
    }

    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam String email) {
        employeeService.deleteEmployee(email);
        return "redirect:/listOfAllEmployeesDB";
    }
}

