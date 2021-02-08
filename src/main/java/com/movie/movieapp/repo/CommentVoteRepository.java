package com.movie.movieapp.repo;

import com.movie.movieapp.entity.Comment;
import com.movie.movieapp.entity.CommentVote;
import com.movie.movieapp.entity.Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote,Long> {

    @Query("select c.vote from CommentVote c where c.comment.id = :commentId")
    List<Boolean> findLikes(@Param("commentId")Long commentId);
    Optional<CommentVote> findByComment_IdAndVoter(long commentId, Principal principal);
    Optional<CommentVote> findByVoterAndComment(Principal voter, Comment comment);
}
