package com.movie.movieapp.service;

import com.movie.movieapp.Roles;
import com.movie.movieapp.controller.comment.CommentDTO;
import com.movie.movieapp.controller.comment.CommentRequest;
import com.movie.movieapp.controller.comment.ParentCommentDTO;
import com.movie.movieapp.controller.comment.VoteRequest;
import com.movie.movieapp.entity.Comment;
import com.movie.movieapp.entity.CommentVote;
import com.movie.movieapp.entity.Movie;
import com.movie.movieapp.entity.Principal;
import com.movie.movieapp.repo.CommentRepository;
import com.movie.movieapp.repo.CommentVoteRepository;
import com.movie.movieapp.repo.MovieRepository;
import com.movie.movieapp.security.TNPrincipal;
import com.movie.movieapp.security.UserHelper;
import org.apache.catalina.User;
import org.apache.commons.collections4.map.LazyMap;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;
    private final CommentVoteRepository commentVoteRepository;
    public CommentService(CommentRepository commentRepository, MovieRepository movieRepository, CommentVoteRepository commentVoteRepository) {
        this.commentRepository = commentRepository;
        this.movieRepository = movieRepository;
        this.commentVoteRepository = commentVoteRepository;
    }
    public void persistOrUpdate( CommentRequest request){
        Movie movie =this.movieRepository.findById(request.getMovieId()).orElseThrow(()-> new NotFoundException("Movie not found"));
        Comment comment;


        if(Objects.nonNull(request.getCommentId())){
            comment = commentRepository.findById(request.getCommentId()).orElseThrow(()-> new NotFoundException("Comment not found"));
            TNPrincipal principal = UserHelper.currentPrincipal();
            if(principal.getId().equals(comment.getCommentor().getId()) || UserHelper.currentPrincipal().hasRole(Roles.ADMIN)){
                throw new ForbiddenException("Principal Not allowed to modify comment");
            }

        }else {
            comment = new Comment();
            if(Objects.nonNull(request.getParentId())){
                Comment parentComment = this.commentRepository.findById(request.getParentId()).orElseThrow(()-> new NotFoundException("Parent comment not found"));
                Optional.ofNullable(parentComment.getParentComment()).ifPresent((c)-> new RuntimeException("Parent comment cannot be a child comment"));
                comment.setParentComment(parentComment);
            }
            comment.setCommentor(UserHelper.currentPrincipal().getPrincipal());

        }
        comment.setComment(request.getComment());
        comment.setActive(true);
        comment.setMovie(movie);
        commentRepository.save(comment);
    }

    public void deactivate(Long id) {
        commentRepository.findById(id).ifPresent(comment -> {
            comment.setActive(false);
            this.commentRepository.save(comment);
        });
    }

    public Map<Long,ParentCommentDTO> findByMovie(Long movieId) {
        Principal principal =UserHelper.currentPrincipal().getPrincipal();

        List<CommentDTO> comments = commentRepository.findActiveMovieAndComments(movieId).stream().map(CommentDTO::new).collect(Collectors.toList());
        comments.forEach(comment -> {
            List<Boolean> likes = this.commentVoteRepository.findLikes(comment.getId());
            comment.setLikes(likes.stream().filter(l->l).count());
            comment.setDislikes(likes.stream().filter(l->!l).count());
            comment.setUserLike(this.commentVoteRepository.findByComment_IdAndVoter(comment.getId(), principal).map(CommentVote::getVote).orElse(null));
        });
        Map<Long,ParentCommentDTO> parentComments = comments.stream().filter(commentDTO -> Objects.isNull(commentDTO.getParentCommentId())).map(c-> new ParentCommentDTO(c)).collect(Collectors.toMap(c->c.getParent().getId(), Function.identity()));
        comments.stream().filter(commentDTO -> Objects.nonNull(commentDTO.getParentCommentId())).forEach(c-> parentComments.get(c.getParentCommentId()).getChildren().add(c));

        return parentComments;
    }

    public CommentDTO vote(Long commentId, VoteRequest request){
        Comment comment = commentRepository.findByIdAndActive(commentId, true).orElseThrow(()-> new NotFoundException("Comment not found"));
        Principal principal =UserHelper.currentPrincipal().getPrincipal();
        Optional<CommentVote> opt = commentVoteRepository.findByVoterAndComment( UserHelper.currentPrincipal().getPrincipal(), comment);
        CommentVote cv;
        if(opt.isPresent()){
            cv = opt.get();
            if(principal.getId().equals(comment.getCommentor().getId())){
                throw new ForbiddenException("Principal Not allowed to modify comment");
            }
            setVote(cv,request);

        }else{
            cv = new CommentVote();
            cv.setVoter(principal);
            setVote(cv,request);
        }
        commentVoteRepository.save(cv);
        CommentDTO commentDTO = new CommentDTO(comment);
        List<Boolean> likes = this.commentVoteRepository.findLikes(comment.getId());
        commentDTO.setLikes(likes.stream().filter(l->l).count());
        commentDTO.setDislikes(likes.stream().filter(l->!l).count());

        return commentDTO;
    }
    private void setVote(CommentVote commentVote, VoteRequest request){
        if(Objects.isNull(request.getVote())){
            commentVote.setVote(null);
        } else if(request.getVote()){
            commentVote.setVote(true);
        }else{
            commentVote.setVote(false);
        }

    }
}
