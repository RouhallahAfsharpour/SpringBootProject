package com.example.login_project.repository;
import com.example.login_project.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainRepository {
    public static List<Employee> allEmployees = new ArrayList<Employee>();
    public static List<Employee> allEmployeesDB = new ArrayList<Employee>();

    public static void addEmployee(String firstName, String lastName, String gender, String email, String tel){
        allEmployees.add(new Employee(firstName, lastName, gender, email, tel));

        String jdbcURL = "jdbc:mysql://localhost:3306/employees";
        String username = "root";
        String password = "1234";

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("insert into workers(first_name, last_name, gender, email, telephone)values(?,?,?,?,?);");
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
        System.out.println("Repository: "+email);

        String jdbcURL = "jdbc:mysql://localhost:3306/employees";
        String username = "root";
        String password = "1234";

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

    public static List<Employee> getAllEmployeesDB() throws SQLException {

        allEmployeesDB.clear();

        String jdbcURL = "jdbc:mysql://localhost:3306/employees";
        String username = "root";
        String password = "1234";
        String QUERY = "SELECT first_name, last_name, gender, email, telephone FROM workers";

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
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
}
