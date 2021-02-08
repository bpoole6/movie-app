package com.movie.movieapp.controller.movie;

import com.movie.movieapp.repo.MovieRepository;
import com.movie.movieapp.security.annotations.AdminRole;
import com.movie.movieapp.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("movie")
public class MovieController {
    private final MovieService movieService;
    private final MovieRepository movieRepository;
    public MovieController(MovieService movieService, MovieRepository movieRepository){
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("{id}")
    public MovieDTO getMovieDetails(@PathVariable("id") Long id){
        return movieService.details(id);
    }

    @GetMapping("search/{partial}")
    public void getMovies(@PathVariable("partial") String partial){

    }

    @GetMapping("typeahead/{partial}")
    public List<MovieDTO> movieTypeAhead(@PathVariable("partial") String partial){
        return movieService.typeAheadTitles(partial).stream()
                .map(MovieDTO::new)
                .collect(Collectors.toList());
    }

    @AdminRole
    @PutMapping
    public ResponseEntity<Object> persistOrUpdate(@RequestBody CreateUpdateMovieRequest request){
        this.movieService.save(request);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @AdminRole
    @DeleteMapping("{id}")
    public void deleteMovie(@PathVariable("id") Long id){
        this.movieService.deactivate(id);

    }
    @PutMapping("{id}/rating")
    public void movieRating(@PathVariable("id") Long movieId, @RequestBody RatingRequest request){
        this.movieService.rating(movieId, request);
    }
}
