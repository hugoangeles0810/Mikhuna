package com.jasoftsolutions.mikhuna.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hugo on 04/03/2015.
 */
public class User implements Serializable{

    public static final int FACEBOOK_LOGIN = 1;
    public static final int OWN_LOGIN = 2;


    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String imgUrl;
    private Date birthday;
    private String password;
    private Integer loginType;
    private String uid;

    private String birthDayString;

    public User() {
    }

    public User(String firstname, String lastname, String email, String gender, String birthDayString, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.imgUrl = imgUrl;
        this.birthDayString = birthDayString;
        this.password = password;
    }

    public User(String firstname, String lastname, String email, String gender, String imgUrl, Date birthday) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.imgUrl = imgUrl;
        this.birthday = birthday;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBirthDayString() {
        return birthDayString;
    }

    public void setDate(String birthDayString) {
        this.birthDayString = birthDayString;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", birthday=" + birthday +
                ", password='" + password + '\'' +
                ", loginType=" + loginType +
                ", uid='" + uid + '\'' +
                ", birthDayString='" + birthDayString + '\'' +
                '}';
    }
}
