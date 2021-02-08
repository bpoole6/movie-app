package com.movie.movieapp.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.movie.movieapp.Roles;
import com.movie.movieapp.entity.Principal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.movie.movieapp.security.SecurityConstants.*;

public class JWTUtils {
    public static JWTCreator.Builder create(Principal principal, Collection<GrantedAuthority> authorityList) {
        boolean hasAdminRole = authorityList.stream().anyMatch(g-> Roles.ADMIN == Roles.valueOf(g.getAuthority()));
        long expiration = hasAdminRole ? ADMIN_EXPIRATION_TIME : EXPIRATION_TIME;
        return JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("roles", authorityList.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("id", principal.getId())
                .withClaim("fName", principal.getFirstName())
                .withClaim("lName", principal.getLastName())
                .withClaim("email", principal.getEmail());
    }
    public static JWTCreator.Builder copyFromDecoded(DecodedJWT jwt){
        List<String> roles = jwt.getClaim("roles").asList(String.class);
        boolean hasAdminRole = roles.stream().anyMatch(g-> Roles.ADMIN == Roles.valueOf(g));
        long expiration = hasAdminRole ? ADMIN_EXPIRATION_TIME : EXPIRATION_TIME;
        return JWT.create()
                .withSubject(jwt.getSubject())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("roles", roles)
                .withClaim("id", jwt.getClaim("id").asLong())
                .withClaim("fName", jwt.getClaim("fName").asString())
                .withClaim("lName", jwt.getClaim("lName").asString())
                .withClaim("email", jwt.getClaim("email").asString());

    }

    public static UsernamePasswordAuthenticationToken getUserDetails(DecodedJWT jwt){
        Principal p = new Principal();
        Function<DecodedJWT,Function<String,String>> func = (decoded)->{
            return (str)-> decoded.getClaim(str).asString();
        };
        p.setEmail(jwt.getClaim("email").asString());
        p.setId(jwt.getClaim("id").asLong());
        p.setFirstName(jwt.getClaim("fName").asString());
        p.setLastName(jwt.getClaim("lName").asString());
        p.setUsername(jwt.getSubject());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(p,null,jwt.getClaim("roles").asList(String.class).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        return token;
    }

}
