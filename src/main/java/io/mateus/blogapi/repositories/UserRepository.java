package io.mateus.blogapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.mateus.blogapi.exception.ResourceNotFoundException;
import io.mateus.blogapi.model.role.Role;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.security.UserPrincipal;
import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@NotBlank String username);

    Optional<User> findByEmail(@NotBlank String email);

    Boolean existsByUsername(@NotBlank String username);

    Boolean existsByEmail(@NotBlank String email);

    Boolean existsByRolesContaining(Role role);

    Optional<User> findByUsernameOrEmail(String username, String email);

    default User getUser(UserPrincipal currentUser) {
		return getUserByName(currentUser.getUsername());
	}

	default User getUserByName(String username) {
		return findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}
}
