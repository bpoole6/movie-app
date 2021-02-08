package com.movie.movieapp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "movie_rating")
public class MovieRating extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private Principal voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private Rating rating;
}
