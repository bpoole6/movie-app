package com.movie.movieapp.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * This class would have been useful as treating titles individual words are keywords and
 * finding the ones that most match with a search.
 *
 * For Example:
 * If somebody were to search Star phantom a search result would pull up "Star Wars: Episode I – The Phantom Menace" as
 * a possible selection.
 */

@Data
@Entity
@Table(name = "movie_title_break_down")
public class MovieTitleBreakDown {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String token;
}
