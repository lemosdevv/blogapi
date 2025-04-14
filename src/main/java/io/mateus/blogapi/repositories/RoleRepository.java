package io.mateus.blogapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.mateus.blogapi.model.role.Role;
import io.mateus.blogapi.model.role.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
