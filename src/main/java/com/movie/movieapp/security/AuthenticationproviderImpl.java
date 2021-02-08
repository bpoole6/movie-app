package com.movie.movieapp.security;

import com.movie.movieapp.Roles;
import com.movie.movieapp.entity.Principal;
import com.movie.movieapp.exceptions.ForbiddenException;
import com.movie.movieapp.repo.PrincipalRepository;
import com.movie.movieapp.repo.PrincipalRolesRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationproviderImpl implements AuthenticationProvider {
    private final PrincipalRepository applicationUserRepository;
    private final PrincipalRolesRepository principalRolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationproviderImpl(PrincipalRepository applicationUserRepository, PrincipalRolesRepository principalRolesRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.principalRolesRepository = principalRolesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        if(ObjectUtils.anyNull(username, password)){
            throw new UsernameNotFoundException("null");
        }
        Principal applicationUser = applicationUserRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
        if(!bCryptPasswordEncoder.matches(password,applicationUser.getPassword())){
            throw new ForbiddenException("Incorrect Username/Password");
        }

        List<GrantedAuthority> grantedAuthorityList = principalRolesRepository
                .findPrincipalRolesByPrincipal(applicationUser).stream().map(s->
                        new SimpleGrantedAuthority(Roles.valueOf(s.getRoleId()).getName())
                ).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(applicationUser, null, grantedAuthorityList);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return  authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
