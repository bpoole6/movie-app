package com.movie.movieapp.security;

import com.movie.movieapp.Roles;
import com.movie.movieapp.entity.Principal;
import com.movie.movieapp.repo.PrincipalRepository;
import com.movie.movieapp.repo.PrincipalRolesRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private PrincipalRepository applicationUserRepository;
    private PrincipalRolesRepository principalRolesRepository;

    public UserDetailsServiceImpl(PrincipalRepository applicationUserRepository,PrincipalRolesRepository principalRolesRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.principalRolesRepository = principalRolesRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Principal> applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> grantedAuthorityList = principalRolesRepository
                .findPrincipalRolesByPrincipal(applicationUser.get()).stream().map(s->
            new SimpleGrantedAuthority(Roles.valueOf(s.getRoleId()).getName())
        ).collect(Collectors.toList());
        return new User(applicationUser.get().getUsername(), applicationUser.get().getPassword(), grantedAuthorityList);
    }
}