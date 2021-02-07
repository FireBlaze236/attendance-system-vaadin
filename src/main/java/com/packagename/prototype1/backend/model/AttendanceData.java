package com.packagename.prototype1.backend.model;

import javax.persistence.*;

/**
 * Attendance Data Model Class for Java Persistent API Entity
 */
@Entity
public class AttendanceData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long aId;
    private String studentId;
    private String studentName;
    private String username;
    @ManyToOne
    @JoinColumn(name = "session_code")
    private SessionData sessionData;
    private String userIp;
    private Integer score;
    private Double scorePercentage;
    private Boolean verdict;

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

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Double getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(Double scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public boolean getVerdict() {
        return verdict;
    }

    public void setVerdict(boolean verdict) {
        this.verdict = verdict;
    }
}
