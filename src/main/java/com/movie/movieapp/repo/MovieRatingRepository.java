package com.movie.movieapp.repo;

import com.movie.movieapp.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {
    @Query("select m.rating from MovieRating m where m.movie.id = :movieId")
    List<Rating> findRatings(@Param("movieId")Long movieId);
    Optional<MovieRating> findByVoterAndMovie(Principal voter, Movie movie);
}
