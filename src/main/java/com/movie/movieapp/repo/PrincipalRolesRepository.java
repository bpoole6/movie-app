package com.movie.movieapp.repo;

import com.movie.movieapp.entity.Principal;
import com.movie.movieapp.entity.PrincipalRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrincipalRolesRepository extends JpaRepository<PrincipalRoles,Long> {
    List<PrincipalRoles> findPrincipalRolesByPrincipal(Principal principal);
}
