package com.dptablo.template.springboot.service.defaults;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dptablo.template.springboot.ApplicationConfiguration;
import com.dptablo.template.springboot.exception.ApplicationErrorCode;
import com.dptablo.template.springboot.exception.ApplicationException;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.repository.UserRepository;
import com.dptablo.template.springboot.security.DefaultUserDetails;
import com.dptablo.template.springboot.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultJwtAuthenticationService implements JwtAuthenticationService {
    private final UserRepository userRepository;
    private final ApplicationConfiguration applicationConfiguration;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void postConstruct() {
        System.out.println("123123");
    }

    @Override
    public User signUp(String userId, String password) {
        User user = User.builder()
                .userId(userId)
                .password(password)
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<String> authenticate(String userId, String password) {
        try {
            User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);
            if(passwordEncoder.matches(password, user.getPassword())) {
                HashSet<GrantedAuthority> authoritySet = user.getUserRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.toString()))
                        .collect(Collectors.toCollection(HashSet::new));

                UserDetails userDetails = createUserDetails(user, authoritySet);
                return Optional.ofNullable(createToken(userDetails));
            } else {
                throw new ApplicationException(ApplicationErrorCode.AUTHENTICATION_ID_OR_PASSWORD_MISMATCH,
                        ApplicationErrorCode.AUTHENTICATION_ID_OR_PASSWORD_MISMATCH.getDescription());
            }
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean verifyToken(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getToken().equals(token);
    }

    @Override
    public Optional<Authentication> getAuthentication(String token) {
        try {
            DecodedJWT decodedJWT = decodeToken(token);

            User user = userRepository.findById(decodedJWT.getSubject()).orElseThrow(NullPointerException::new);
            Collection<GrantedAuthority> authorityCollection =
                    user.getUserRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toSet());

            return Optional.ofNullable(new UsernamePasswordAuthenticationToken(user.getUserId(), null, authorityCollection));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    private String createToken(UserDetails userDetails) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(applicationConfiguration.getJwtPrivateKey());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(applicationConfiguration.getJwtIssUser())
                .withExpiresAt(new Date(
                        System.currentTimeMillis() + (1000 * 60 * applicationConfiguration.getJwtExpiryMinutes())
                ))
                .sign(algorithm);
    }

    private DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(applicationConfiguration.getJwtPrivateKey());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(applicationConfiguration.getJwtIssUser())
                .build();
        return verifier.verify(token);
    }

    private UserDetails createUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        return DefaultUserDetails.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .enable(true)
                .authorities(authorities)
                .build();
    }
}
