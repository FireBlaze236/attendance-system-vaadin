package com.packagename.prototype1.backend.model;

import net.bytebuddy.utility.RandomString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class SessionData {
    @Id
    private String sessionCode;
    private String sessionName;
    private String ownerUser;
    private Timestamp sessionStartTime;
    private Timestamp sessionEndTime;


    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = RandomString.hashOf((String.valueOf(LocalDateTime.now().getSecond()) + sessionCode)
                .hashCode());
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(String ownerUser) {
        this.ownerUser = ownerUser;
    }

    public Timestamp getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(Timestamp sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public Timestamp getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(Timestamp sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

}
