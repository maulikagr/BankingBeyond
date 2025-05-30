package com.CSA.BankingBeyond;

/*
 * This is the class for the user.
 * It is used to create a user object.
 * It is also used to get the username and password of the user.
 */

public class User {
    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}