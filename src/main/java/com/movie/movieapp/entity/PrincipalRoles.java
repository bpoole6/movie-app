package com.movie.movieapp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "principal_roles")
@IdClass(PrincipalRolesId.class)
public class PrincipalRoles {

    @Id
    private Integer roleId;

    @Id
    @ManyToOne
    @JoinColumn(name = "principal_id")
    private Principal principal;
}
