package com.movie.movieapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.movie.movieapp.entity.Principal;
import com.movie.movieapp.exceptions.ForbiddenException;
import com.movie.movieapp.security.jwt.JWTUtils;
import com.movie.movieapp.security.token_blacklist.BlackListed;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.movie.movieapp.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final EncryptedKeysService encryptedKeysService;
    private final BlackListed blackListed;
    public JWTAuthorizationFilter(AuthenticationManager authManager, EncryptedKeysService encryptedKeysService, BlackListed blackListed) {
        super(authManager);
        this.encryptedKeysService = encryptedKeysService;
        this.blackListed = blackListed;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        String token = req.getHeader(HEADER_STRING);

        if (token != null) {
            if(blackListed.isBlock(token)){
                throw new ForbiddenException("Not Authorized");
            }
            // parse the token.
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(encryptedKeysService.getSecret().getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));

            UsernamePasswordAuthenticationToken auth = JWTUtils.getUserDetails(jwt);
            String newToken = JWTUtils.copyFromDecoded(jwt).sign(HMAC512(encryptedKeysService.getSecret().getBytes()));
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                res.addHeader(HEADER_STRING, TOKEN_PREFIX + newToken);
                chain.doFilter(req, res);
            }
        }
    }

}