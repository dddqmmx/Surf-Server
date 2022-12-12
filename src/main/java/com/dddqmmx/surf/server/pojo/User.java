package com.dddqmmx.surf.server.pojo;

import java.io.Serializable;

public class User implements Serializable {

    private Integer id;
    private String userName;
    private String password;
    private String name;

    private String personalProfile;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonalProfile() {
        return personalProfile;
    }

    public void setPersonalProfile(String personal_profile) {
        this.personalProfile = personal_profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", personalProfile='" + personalProfile + '\'' +
                '}';
    }
}
