package com.example.login_project.service;

import com.example.login_project.model.Employee;
import com.example.login_project.model.QuestionBank;
import com.example.login_project.repository.MainRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@org.springframework.stereotype.Service
public class MainService {

    public static boolean checkEmail(String email){
        return MainRepository.checkEmail(email);
    }

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

    public static List<QuestionBank> getAllQuestions() throws SQLException {
        return MainRepository.getAllQuestions();
    }
    //Updated
    public static List<QuestionBank> getAllQuestionsUpdated() throws SQLException {
        return MainRepository.getAllQuestionsUpdated();
    }
    public static void sendAnswer(int questionID, int ans) throws SQLException {
        MainRepository.deleteQuestion(questionID);
        MainRepository.sendAnswer(questionID,ans);
    }
}
