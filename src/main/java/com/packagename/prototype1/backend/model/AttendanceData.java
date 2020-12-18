package com.packagename.prototype1.backend.model;

import javax.persistence.*;

@Entity
public class AttendanceData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long aId;
    private String studentId;
    private String studentName;
    @Column(unique = true)
    private String username;
    @ManyToOne
    @JoinColumn(name = "session_code")
    private SessionData sessionData;

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }
}
