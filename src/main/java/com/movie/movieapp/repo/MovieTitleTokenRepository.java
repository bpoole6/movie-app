package com.movie.movieapp.repo;

import com.movie.movieapp.entity.Movie;
import com.movie.movieapp.entity.MovieTitleToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieTitleTokenRepository extends JpaRepository<MovieTitleToken,Long> {

    List<MovieTitleToken> findMovieTitleTokenByMovie_Id(long movieId);


}
