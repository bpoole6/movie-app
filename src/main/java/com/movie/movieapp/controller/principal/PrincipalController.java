package com.movie.movieapp.controller.principal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("login")
public class PrincipalController {

    @PostMapping
    public PrincipalDTO login(@RequestBody LoginRequest request){

    }


}
