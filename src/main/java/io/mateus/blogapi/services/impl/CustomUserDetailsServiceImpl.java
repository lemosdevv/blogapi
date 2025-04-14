package io.mateus.blogapi.services.impl;

import io.mateus.blogapi.security.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.repositories.UserRepository;
import io.mateus.blogapi.services.CustomUserDetailsService;
import jakarta.transaction.Transactional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /*
     * Implementação do método loadUserByUsername para buscar um usuário pelo nome
     * de usuário ou e-mail.
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username or email: %s", usernameOrEmail)));

        return UserPrincipal.create(user);
    }

    // Implementação do método loadUserById para buscar um usuário pelo ID.
    @Override
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username or email: %s", id)));

        return UserPrincipal.create(user);
    }

}
