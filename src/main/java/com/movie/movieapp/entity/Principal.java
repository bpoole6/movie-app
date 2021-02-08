package com.movie.movieapp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "principal")
public class Principal extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    public Long getId() {
        return id;
    }
}
