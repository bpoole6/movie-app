package com.movie.movieapp.repo;

import com.movie.movieapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c inner join Movie m on c.movie.id = m.id where m.id = :movieId and c.active = true and m.active = true")
    List<Comment> findActiveMovieAndComments(@Param("movieId") Long movieId);
    Optional<Comment> findByIdAndActive(Long id, boolean active);
}
