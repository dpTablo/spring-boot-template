package com.dptablo.template.springboot.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dptablo.template.springboot.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface JwtAuthenticationService {
    User signUp(String userId, String password);
    Optional<String> authenticate(String userId, String password);
    boolean verifyToken(String token) throws JWTVerificationException;
    Optional<Authentication> getAuthentication(String token);
}
