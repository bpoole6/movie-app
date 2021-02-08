package com.movie.movieapp.controller.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CommentRequest {
    @NotNull
    private String comment;
    @NotNull
    private Long movieId;

    private Long commentId;

    private Long parentId;

}
