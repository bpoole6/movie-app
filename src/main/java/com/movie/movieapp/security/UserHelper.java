package com.movie.movieapp.security;

import com.movie.movieapp.entity.Principal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserHelper {

    public static TNPrincipal currentPrincipal(){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return new TNPrincipal((Principal) authentication.getPrincipal(),authentication.getAuthorities());
    }
}
