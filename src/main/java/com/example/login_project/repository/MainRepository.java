package com.example.login_project.repository;
import com.example.login_project.model.Employee;
import com.example.login_project.model.QuestionBank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainRepository {
    public static List<Employee> allEmployees = new ArrayList<>();
    public static List<Employee> allEmployeesDB = new ArrayList<>();
    public static List<QuestionBank> allQuestions = new ArrayList<>();
    public static String jdbcURL = "jdbc:mysql://localhost:3306/employees";
    public static String username = "root";
    public static String password = "1234";


    public static void addEmployee(String firstName, String lastName, String gender, String email, String tel){
        allEmployees.add(new Employee(firstName, lastName, gender, email, tel));

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO workers(first_name, last_name, gender, email, telephone)values(?,?,?,?,?);");
            ps.setString(1,firstName);
            ps.setString(2,lastName);
            ps.setString(3,gender);
            ps.setString(4,email);
            ps.setString(5,tel);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //deleteUser
    public static void deleteEmployee(String email){

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM workers WHERE email = ?");
            ps.setString(1,email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> getAllEmployees(){
        List<Employee> employees = allEmployees;
        return employees;
    }

    public  static boolean checkEmail(String email){
        boolean check =false;
        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT email FROM workers");
        ) {
            while(rs.next()){
                if(rs.getString("email").equals(email)){
                    check=true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    public static List<Employee> getAllEmployeesDB() throws SQLException {

        allEmployeesDB.clear();

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT first_name, last_name, gender, email, telephone FROM workers");
        ) {
            while(rs.next()){
                allEmployeesDB.add(new Employee(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("email"),
                        rs.getString("telephone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Employee> EmployeesFromDB = allEmployeesDB;
        return EmployeesFromDB;
    }

    public static List<QuestionBank> getAllQuestions() throws SQLException {

        allQuestions.clear();

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, question, option1, option2, option3, option4, answer FROM questions");
        ) {
            while(rs.next()){
                allQuestions.add(new QuestionBank(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getString("answer")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<QuestionBank> allQuestionsFromDB = allQuestions;
        return allQuestionsFromDB;
    }

    //Updated
    public static List<QuestionBank> getAllQuestionsUpdated(){
        return allQuestions;
    }

    public static void sendAnswer(int questionID, int ans){
        String email ="abadanastro@gmail.com";
        System.out.println(questionID+"= "+ans);

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO answers(question_id, submitted_answer)values(?,?);");
            ps.setInt(1,questionID);
            ps.setInt(2,ans);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteQuestion(int questionID){

        List<QuestionBank> allQuestionsUp = getAllQuestionsUpdated();
        /*for(QuestionBank question : allQuestionsUp){
            if(question.getId()==questionID){
                allQuestionsUp.remove(question);
            }
        }*/
        allQuestionsUp.removeIf(obj -> obj.getId() == questionID);
        System.out.println("s"+allQuestionsUp.size());
        allQuestions=allQuestionsUp;
    }
}
