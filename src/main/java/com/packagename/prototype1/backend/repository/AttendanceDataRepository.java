package com.packagename.prototype1.backend.repository;

import com.packagename.prototype1.backend.model.AttendanceData;
import com.packagename.prototype1.backend.model.SessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Data Repository Interface for Attendance Data
 */
@Repository
public interface AttendanceDataRepository extends JpaRepository<AttendanceData, Long> {
    /**
     * Template function for finding Attendance Data by Session Data, created by Spring Data JPA
     * @param sessionData
     * @return
     */
    List<AttendanceData> findBySessionData(SessionData sessionData);
    //Optional<AttendanceData> findByUsername(String username);

    /**
     * Template function for finding Attendance Data by Session Data, Username , created by Spring Data JPA
     * @param username
     * @param sessionData
     * @return
     */
    Optional<AttendanceData> findByUsernameAndSessionData(String username, SessionData sessionData);
}
