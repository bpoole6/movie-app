package com.movie.movieapp.controller.principal;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
