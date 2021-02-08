package com.movie.movieapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class PrincipalRolesId implements Serializable {
    private Integer roleId;


    private Principal principal;

    public PrincipalRolesId(Integer roleId, Principal principal) {
        this.roleId = roleId;
        this.principal = principal;
    }
}
