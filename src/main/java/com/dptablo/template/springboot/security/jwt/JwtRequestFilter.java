package com.dptablo.template.springboot.security.jwt;

import com.dptablo.template.springboot.service.JwtAuthenticationService;
import com.dptablo.template.springboot.service.defaults.DefaultJwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenInRequest(request);
//        if(StringUtils.hasText(token) && jwtAuthenticationService.verifyToken(token)) {
//            Authentication authentication = jwtAuthenticationService.getAuthentication(token).orElseThrow(ServletException::new);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
        filterChain.doFilter(request, response);
    }

    private String getTokenInRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
