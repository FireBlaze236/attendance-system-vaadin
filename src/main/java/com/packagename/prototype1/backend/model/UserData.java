package com.packagename.prototype1.backend.model;

import javax.persistence.*;

/**
 * User Data Model Class for Java Persistent API Entity
 */
@Entity
@Table(name = "users")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(length = 2048)
    private String password;
    @Column(name = "enabled")
    private boolean active;
    private String roles;

    /**
     * Transactional Data Needs an Empty Constructor in JPA. JPA Defaults empty constructor automatically
     */
    public UserData()
    {

    }

    /**
     * Constructor for generating user with custom id
     * @param id
     */
    public UserData(Long id)
    {
        this.id = id;
    }

    /**
     * New user object constructor to create a user model for insertion into the database.
     * @param username
     * @param password
     */
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
