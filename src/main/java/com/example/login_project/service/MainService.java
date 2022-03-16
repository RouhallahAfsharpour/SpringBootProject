package com.example.login_project.service;

import com.example.login_project.model.CityWeather;
import com.example.login_project.model.Currency;
import com.example.login_project.model.Employee;
import com.example.login_project.model.QuestionBank;
import com.example.login_project.repository.MainRepository;
import com.sun.tools.javac.Main;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@org.springframework.stereotype.Service
public class MainService {

    public static boolean checkEmail(String email){
        return MainRepository.checkEmail(email);
    }

    public static String getFirstName(String email){
        return MainRepository.getFirstName(email);
    }

    public static boolean validateUser(String email,String pass){
        boolean check=false;
        if(checkEmail(email)){
            if (MainRepository.checkPass(email,pass)){
                check= true;
            }
        }else {
            check= false;
        }
        return check;
    }

    public static void addNewEmployee(String firstName, String lastName, String gender, String email, String tel, String password){
        MainRepository.addEmployee(firstName, lastName, gender, email, tel, password);
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

    public static List<String> getAllQuestionnaireTableNames(){
        return MainRepository.getAllQuestionnaireTableNames();
    }

    public static List<QuestionBank> getAllQuestions(String questionnaireName) throws SQLException {
        return MainRepository.getAllQuestions(questionnaireName);
    }
    //Updated
    public static List<QuestionBank> getAllQuestionsUpdated() throws SQLException {
        return MainRepository.getAllQuestionsUpdated();
    }

    public static void createNewQuestionnaireInDB(String questionnaireName) throws SQLException {
        MainRepository.createNewQuestionnaireInDB(questionnaireName);
    }

    public static void addNewQuestion(String questionnaire,String question, String option1, String option2, String option3, String option4, String answer){
        MainRepository.addNewQuestion(questionnaire,question,option1,option2,option3,option4,answer);
    }

    public static void sendAnswer(int questionID, int ans) throws SQLException {
        MainRepository.deleteQuestion(questionID);
        MainRepository.sendAnswer(questionID,ans);
    }

    public static void deleteQuestionnaire(String questionnaire){
        MainRepository.deleteQuestionnaire(questionnaire);
    }

    public static QuestionBank getQuestionForEdit(String questionnaireName,int questionID){
        return MainRepository.getQuestionForEdit(questionnaireName,questionID);
    }

    public static void editThisQuestion(String questionnaire, int questionID,String question, String option1, String option2, String option3, String option4, String answer){
        MainRepository.editThisQuestion(questionnaire,questionID,question,option1,option2,option3,option4,answer);
    }

    public static void deleteQuestionFromQuestionnaire(String questionnaireName,int questionID){
        MainRepository.deleteQuestionFromQuestionnaire(questionnaireName,questionID);
    }

    public static Double calculateFromUSD(Double amount,String currencyName) throws ExecutionException, InterruptedException {
        return MainRepository.calculateFromUSD(amount,currencyName);
    }

    public static Double convertCurrency(Double amount,String currencyFrom,String currencyTo) throws ExecutionException, InterruptedException {
        return MainRepository.convertCurrency(amount,currencyFrom,currencyTo);
    }

    public static List<Currency> allCurrencies() throws ExecutionException, InterruptedException {
        return MainRepository.allCurrencies();
    }

    public static List<CityWeather> weatherAppGermany() throws ExecutionException, InterruptedException {
        return MainRepository.weatherAppGermany();
    }
}
