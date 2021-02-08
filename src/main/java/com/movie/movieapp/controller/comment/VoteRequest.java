package com.movie.movieapp.controller.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VoteRequest {
    private Boolean vote;

}
