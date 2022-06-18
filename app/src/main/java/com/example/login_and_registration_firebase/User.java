package com.example.login_and_registration_firebase;

public class User {
    public String fullname,username,password,mobile_no;

    public User() {
    }

    public User(String fullname, String username, String password,String mobile_no) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.mobile_no = mobile_no;

    }


}
