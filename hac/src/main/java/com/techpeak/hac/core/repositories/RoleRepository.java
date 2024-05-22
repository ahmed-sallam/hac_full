package com.techpeak.hac.core.repositories;

import java.util.Optional;

import com.techpeak.hac.core.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import com.techpeak.hac.core.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
