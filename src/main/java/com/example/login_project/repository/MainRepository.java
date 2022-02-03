package com.example.login_project.repository;
import com.example.login_project.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainRepository {
    public static List<User> allUsers = new ArrayList<User>();
    public static List<User> allUsersDB = new ArrayList<User>();

    public static void addUser(String firstName, String lastName, String email, String tel, String gender){
        allUsers.add(new User(firstName, lastName, email, tel, gender));

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

    public static List<User> getAllUsers(){
        List<User> users = allUsers;
        return users;
    }

    public static List<User> getAllUsersDB() throws SQLException {

        allUsersDB.clear();

        String jdbcURL = "jdbc:mysql://localhost:3306/employees";
        String username = "root";
        String password = "1234";
        String QUERY = "SELECT first_name, last_name, gender, email, telephone FROM workers";

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
        ) {
            while(rs.next()){
                allUsersDB.add(new User(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("email"),
                        rs.getString("telephone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<User> usersFromDB = allUsersDB;
        return usersFromDB;
    }
}
