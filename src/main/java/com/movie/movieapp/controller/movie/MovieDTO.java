package com.movie.movieapp.controller.dto;

import com.movie.movieapp.entity.Movie;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate premiereDate;
    private ZonedDateTime createdAt;

    public MovieDTO(Movie movie){
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.premiereDate = movie.getPremiereDate();
        this.createdAt = movie.getCreatedAt();
    }
}
