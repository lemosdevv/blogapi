package io.mateus.blogapi.security;

import java.util.Date;

import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    // Logger para registrar erros e informações
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    // Segredo para assinar o token (pegado do application.properties)
    @Value(value = "${app.jwtSecret}")
    private String jwtSecret;

    // Tempo de expiração do token em milissegundos (também vem do application.properties)
    @Value(value = "${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    // Gera o token JWT com base nas informações do usuário autenticado
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal(); // pega o usuário autenticado

        Date now = new Date(); // data atual
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs); // data de expiração do token

        // Constrói o token JWT com ID do usuário, data atual, expiração e assina com o segredo
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId())) // ID do usuário como "dono" do token
                .setIssuedAt(now) // data de criação
                .setExpiration(expiryDate) // data de expiração
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())) // assinatura com chave HMAC
                .compact(); // finaliza o token
    }

    // Extrai o ID do usuário a partir do token JWT
    public Long getUserIdFromJWT(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject()); // pega o ID que foi definido no .setSubject()
    }

    // Valida se o token JWT é legítimo
    public boolean validateToken(String authToken) {
        try {
            parseClaims(authToken); // tenta parsear o token
            return true; // se não deu erro, o token é válido
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (SignatureException ex) {
            LOGGER.error("Invalid JWT signature");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty");
        }
        return false; // se deu qualquer erro, o token não é válido
    }

    // Método auxiliar para extrair os claims do token JWT
    private Claims parseClaims(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes())) // configura a chave de assinatura
                .build();

        return parser.parseClaimsJws(token).getBody(); // retorna os claims extraídos do token
    }
}
