package com.movie.movieapp.controller.comment;

import com.movie.movieapp.controller.principal.PrincipalDTO;
import com.movie.movieapp.entity.Comment;
import com.movie.movieapp.util.TimeUtils;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class CommentDTO {

    private Long id;

    private PrincipalDTO commentor;

    private long movieId;

    private String comment;

    private boolean active;

    private String createdAt;

    private ZonedDateTime createdAtDate;

    private Long parentCommentId;

    private long likes;

    private long dislikes;

    private Boolean userLike;

    private final List<CommentDTO> commentList = new ArrayList<>();

    public CommentDTO(Comment other) {
        this.id = other.getId();
        this.commentor = new PrincipalDTO(other.getCommentor());
        this.movieId = other.getMovie().getId();
        this.comment = other.getComment();
        this.active = other.isActive();
        this.createdAt = TimeUtils.ZONED_DATE_TIME.format(other.getCreatedAt());
        this.createdAtDate = other.getCreatedAt();
        Optional.ofNullable(other.getParentComment()).ifPresent(c-> setParentCommentId(c.getId()));
    }
}
