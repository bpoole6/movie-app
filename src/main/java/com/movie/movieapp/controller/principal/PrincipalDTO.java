package com.movie.movieapp.controller.principal;

import com.movie.movieapp.entity.Principal;
import lombok.Data;

@Data
public class PrincipalDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public PrincipalDTO(Principal other) {
        this.id = other.getId();
        this.username = other.getUsername();
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.email = other.getEmail();
    }
}
