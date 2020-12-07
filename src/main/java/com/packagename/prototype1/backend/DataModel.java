package com.packagename.prototype1.backend;

import javax.persistence.*;

@Entity
public class DataModel {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "student_id", unique = true)
    private int student_id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private int code;

    public DataModel()
    {

    }

    public DataModel(int student_id, String name, int code)
    {
        this.student_id = student_id;
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
