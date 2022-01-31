package com.example.login_project.model;

public class User {
    protected String firstName ="";
    protected String LastName ="";
    protected String email ="";
    protected String password ="";

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.LastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
