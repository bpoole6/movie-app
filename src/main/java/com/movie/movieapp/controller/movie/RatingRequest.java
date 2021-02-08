package com.movie.movieapp.controller.movie;

import com.movie.movieapp.entity.Rating;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RatingRequest {
    @NotNull
    private Rating rating;
}
