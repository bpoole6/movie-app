package com.movie.movieapp.repo;

import com.movie.movieapp.entity.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal,Long> {
    Optional<Principal> findByUsername(String username);
}
