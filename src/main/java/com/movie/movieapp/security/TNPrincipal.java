package com.movie.movieapp.security;

import com.movie.movieapp.Roles;
import com.movie.movieapp.entity.Principal;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TNPrincipal {
    private final Principal principal;
    private final List<Roles> roles;

    public TNPrincipal(Principal principal, Collection<GrantedAuthority> authorities) {
        this.principal = principal;
        this.roles = List.copyOf(authorities.stream().map(g-> Roles.valueOf(g.getAuthority())).collect(Collectors.toList()));
    }

    public Principal getPrincipal() {
        return principal;
    }

    public List<Roles> getRoles() {
        return roles;
    }
    public boolean hasRole(Roles role){
        return roles.contains(role);
    }

    public Long getId() {
        return principal.getId();
    }

    public String getUsername() {
        return principal.getUsername();
    }

    public String getPassword() {
        return principal.getPassword();
    }

    public String getFirstName() {
        return principal.getFirstName();
    }

    public String getLastName() {
        return principal.getLastName();
    }

    public String getEmail() {
        return principal.getEmail();
    }
}
