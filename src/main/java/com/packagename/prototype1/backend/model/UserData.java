package com.packagename.prototype1.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(length = 2048)
    private String password;
    @Column(name = "enabled")
    private boolean active;
    private String roles;

    public UserData()
    {

    }
    public UserData(Long id)
    {
        this.id = id;
    }

    public UserData(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.active = true;
        this.roles = "USER";
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }
}
