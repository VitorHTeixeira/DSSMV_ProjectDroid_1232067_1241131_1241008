package com.lvhm.covertocover.models;

import java.util.Date;

public class User {
    private String name;
    private String email;
    private String password;
    private String sex;
    private Date birthdate;
    private String phone_number;
    private String address;

    public User(String name, String email, String password, String sex, Date birthdate, String phone_number, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.birthdate = birthdate;
        this.phone_number = phone_number;
        this.address = address;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public Date getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
