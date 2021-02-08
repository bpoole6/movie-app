package com.movie.movieapp.controller.comment;

import com.movie.movieapp.repo.CommentRepository;
import com.movie.movieapp.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    public CommentController(CommentService commentService, CommentRepository commentRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

    @PutMapping()
    public ResponseEntity<Object> persistOrUpdateComment(@RequestBody CommentRequest request){
        this.commentService.persistOrUpdate(request);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deactivateComment(@PathVariable("id") Long id){
        this.commentService.deactivate(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Map<Long,ParentCommentDTO>> commentsByMovie(@PathVariable("movieId")Long movieId){
        return new ResponseEntity<>(this.commentService.findByMovie(movieId), HttpStatus.OK);

    }

    @PutMapping("{id}/rating")
    public ResponseEntity<CommentDTO> thumbComment(@PathVariable("id")Long commentId , @RequestBody VoteRequest voteRequest){
        return new ResponseEntity<>(this.commentService.vote(commentId, voteRequest),HttpStatus.OK);
    }
}
