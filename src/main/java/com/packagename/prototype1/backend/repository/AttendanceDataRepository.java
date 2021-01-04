package com.packagename.prototype1.backend.repository;

import com.packagename.prototype1.backend.model.AttendanceData;
import com.packagename.prototype1.backend.model.SessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceDataRepository extends JpaRepository<AttendanceData, Long> {
    List<AttendanceData> findBySessionData(SessionData sessionData);
    //Optional<AttendanceData> findByUsername(String username);
    Optional<AttendanceData> findByUsernameAndSessionData(String username, SessionData sessionData);
}
