package com.backend.eTrade.repositories.users;

import com.backend.eTrade.models.users.AppRole;
import com.backend.eTrade.models.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
