package com.movie.movieapp.controller;

import com.movie.movieapp.controller.dto.MovieDTO;
import com.movie.movieapp.entity.Movie;
import com.movie.movieapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("movie")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }
    public void getMovieDetails(){

    }

    public void getMovies(Object search){

    }

    @GetMapping("typeahead/{partial}")
    public List<MovieDTO> movieTypeAhead(@PathVariable("partial") String partial){
        return movieService.typeAheadTitles(partial).stream()
                .map(MovieDTO::new)
                .collect(Collectors.toList());
    }

    public void updateMovie(){

    }
    public void deleteMovie(){

    }
    public void deleteComment(){

    }
}
