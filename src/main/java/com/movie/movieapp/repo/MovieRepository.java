package com.movie.movieapp.repo;

import com.movie.movieapp.entity.Movie;
import com.movie.movieapp.entity.Principal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
//    @Query("select m from Movie m where Lower(m.title) like LOWER(%:title)")
//    Optional<Movie> findByUsername(@Param("title") String title);
    List<Movie> findByTitleIgnoreCaseStartsWith(String title, Pageable pageable);

    @Query("select m from Movie m where m.active = true and m.id =:id")
    Optional<Movie> findActiveMovieById(@Param("id") Long id);

    @Query("select distinct  m from Movie m inner join MovieTitleToken mtt on m.id = mtt.movie.id where mtt.token in (:tokens) ")
    List<Movie> getByTokenIn(@Param("tokens") List<String> tokens, Pageable pageable);
}
