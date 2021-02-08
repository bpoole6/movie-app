package com.movie.movieapp.repo;

import com.movie.movieapp.entity.Movie;
import com.movie.movieapp.entity.MovieTitleBreakDown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieTitleBreakDownRepository extends JpaRepository<MovieTitleBreakDown,Long> {

    @Query("select m from MovieTitleBreakDown  where movie.id = :id and token = ")
}
