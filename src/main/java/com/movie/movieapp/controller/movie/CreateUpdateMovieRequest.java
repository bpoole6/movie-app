package com.movie.movieapp.controller.movie;

import com.movie.movieapp.entity.Movie;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CreateUpdateMovieRequest {
    private Long id;
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private LocalDate premiereDate;

}
