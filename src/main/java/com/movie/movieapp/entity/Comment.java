package com.movie.movieapp.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "commentor_id")
    private Principal commentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String comment;

    private boolean active;

    private ZonedDateTime createdAt;

    @ManyToOne()
    @JoinColumn(name = "commented_id")
    private Comment parentComment;


}
