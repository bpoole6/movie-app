package com.movie.movieapp.controller.comment;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParentCommentDTO {
    private final CommentDTO parent;
    private final List<CommentDTO> children = new ArrayList<>();
}
