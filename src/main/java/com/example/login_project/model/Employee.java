package com.example.login_project.model;

public class Employee {
    private String firstName ="";
    private String lastName ="";
    private String email ="";
    private String tel ="";
    private String gender ="";
    private String password ="";

    public Employee(String firstName, String lastName, String email, String tel, String gender, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.gender = gender;
        this.password = password;
    }

    public Employee(String firstName, String lastName, String gender, String email, String tel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.gender = gender;
    }

    public Employee() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }
}
