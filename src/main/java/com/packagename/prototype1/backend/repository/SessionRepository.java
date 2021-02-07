package com.packagename.prototype1.backend.repository;

import com.packagename.prototype1.backend.model.SessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * JPA Data Repository Interface for Session Data
 */
@Repository
@Transactional
public interface SessionRepository extends JpaRepository<SessionData, String> {
    /**
     * Template function for finding Session Data by session code, created by Spring Data JPA
     * @param sessionCode
     * @return
     */
    Optional<SessionData> findBySessionCode(String sessionCode);

    /**
     * Template function for finding Session Data by session name, created by Spring Data JPA
     * @param sessionName
     * @return
     */
    Optional<SessionData> findBySessionName(String sessionName);

    /**
     * Template function for finding Session Data by owner user, created by Spring Data JPA
     * @param ownerUser
     * @return
     */
    List<SessionData> findByOwnerUser(String ownerUser);
}
