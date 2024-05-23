package com.techpeak.hac.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpeak.hac.core.models.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN  u.roles r " +
            "LEFT JOIN  r.privileges " +
            "WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String usename);

}
