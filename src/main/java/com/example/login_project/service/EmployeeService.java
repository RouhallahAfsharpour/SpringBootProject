package com.example.login_project.service;

import com.example.login_project.model.Employee;
import com.example.login_project.repository.MainRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class EmployeeService {

    public static void addNewEmployee(String firstName, String lastName, String gender, String email, String tel){
        MainRepository.addEmployee(firstName, lastName, gender, email, tel);
    }

    public static void deleteEmployee(String email){
        MainRepository.deleteEmployee(email);
    }

    public static List<Employee> getAllEmployees(){
        return MainRepository.getAllEmployees();
    }

    public static List<Employee> getAllEmployeesDB() throws SQLException {
        return MainRepository.getAllEmployeesDB();
    }
}
