package com.packagename.prototype1.backend.repository;

import com.packagename.prototype1.backend.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Data Repository Interface for User Data
 */
@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {
    /**
     * Template function for finding User Data by username, created by Spring Data JPA
     * @param userName
     * @return
     */
    Optional<UserData> findByUsername(String userName);
}
