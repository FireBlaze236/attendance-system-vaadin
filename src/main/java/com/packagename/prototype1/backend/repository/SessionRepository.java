package com.packagename.prototype1.backend.repository;

import com.packagename.prototype1.backend.model.SessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface SessionRepository extends JpaRepository<SessionData, String> {
    Optional<SessionData> findBySessionCode(String sessionCode);
    Optional<SessionData> findBySessionName(String sessionName);
    List<SessionData> findByOwnerUser(String ownerUser);
}
