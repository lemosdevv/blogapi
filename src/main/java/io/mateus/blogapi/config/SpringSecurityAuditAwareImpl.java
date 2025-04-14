package io.mateus.blogapi.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.mateus.blogapi.security.UserPrincipal;

class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {

    // Método responsável por obter o auditor atual. Aqui, o auditor é o usuário autenticado.
    @Override
    public Optional<Long> getCurrentAuditor() {
        // Obtém a autenticação do contexto de segurança.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Optional.ofNullable(userPrincipal.getId());
    }
}


