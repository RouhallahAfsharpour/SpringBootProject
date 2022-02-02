package com.example.login_project.model;

public class User {
    private String firstName ="";
    private String lastName ="";
    private String email ="";
    private String tel ="";
    private String gender ="";

    public User(String firstName, String lastName, String email, String tel, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.gender = gender;
    }

    public User() {
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
}
