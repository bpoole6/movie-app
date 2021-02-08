package com.movie.movieapp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comment_vote")
public class CommentVote extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private Principal voter;

    private Boolean vote;

}
