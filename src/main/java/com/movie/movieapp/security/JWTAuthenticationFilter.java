package com.movie.movieapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movieapp.entity.Principal;
import com.movie.movieapp.security.jwt.JWTUtils;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.movie.movieapp.security.SecurityConstants.HEADER_STRING;
import static com.movie.movieapp.security.SecurityConstants.TOKEN_PREFIX;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final EncryptedKeysService encryptedKeysService;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, EncryptedKeysService encryptedKeysService) {
        this.authenticationManager = authenticationManager;
        this.encryptedKeysService = encryptedKeysService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

        try {
            PrincipalDTO creds = new ObjectMapper()
                    .readValue(req.getInputStream(), PrincipalDTO.class);


            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                           creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Data
    public static class PrincipalDTO{
        private String username;
        private String password;
    }
    @Override


    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =(UsernamePasswordAuthenticationToken)auth;
        Principal principal = (Principal) usernamePasswordAuthenticationToken.getPrincipal();
        String token = JWTUtils.create(principal,usernamePasswordAuthenticationToken.getAuthorities())
                .sign(HMAC512(encryptedKeysService.getSecret().getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}