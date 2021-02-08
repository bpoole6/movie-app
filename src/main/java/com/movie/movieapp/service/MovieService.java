package com.movie.movieapp.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.movie.movieapp.Configuration;
import com.movie.movieapp.Roles;
import com.movie.movieapp.controller.comment.CommentDTO;
import com.movie.movieapp.controller.comment.VoteRequest;
import com.movie.movieapp.controller.movie.CreateUpdateMovieRequest;
import com.movie.movieapp.controller.movie.MovieDTO;
import com.movie.movieapp.controller.movie.RatingRequest;
import com.movie.movieapp.entity.*;
import com.movie.movieapp.repo.MovieRatingRepository;
import com.movie.movieapp.repo.MovieRepository;
import com.movie.movieapp.repo.MovieTitleTokenRepository;
import com.movie.movieapp.security.TNPrincipal;
import com.movie.movieapp.security.UserHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MovieService {


    private final MovieRepository movieRepository;
    private final MovieRatingRepository movieRatingRepository;
    private final MovieTitleTokenRepository movieTitleTokenRepository;
    private final AmazonS3 amazonS3Client;
    @Value("${s3.bucket}")
    private String bucketName;
    public MovieService(MovieRepository movieRepository, MovieRatingRepository movieRatingRepository, MovieTitleTokenRepository movieTitleTokenRepository) {
        this.movieRepository = movieRepository;
        this.movieRatingRepository = movieRatingRepository;
        this.movieTitleTokenRepository = movieTitleTokenRepository;
        this.amazonS3Client = Configuration.buildAWSClients(AmazonS3ClientBuilder.standard());
    }

    public List<Movie> typeAheadTitles(String title){
        List<String> list = Arrays.asList(breakString(title)).stream().map(String::toLowerCase).collect(Collectors.toList());
        return movieRepository.getByTokenIn(list, PageRequest.of(0,20) );
    }

    public MovieDTO details(Long id){
        return movieRepository.findActiveMovieById(id).map(m-> new MovieDTO(m,bucketName)).orElseThrow(() -> new NotFoundException("Movie not found"));

    }

    public void save(CreateUpdateMovieRequest request){
        List<String> wordList = new ArrayList<>();
        String[] words = breakString(request.getTitle());
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if(!word.isEmpty()){
                wordList.add(word);
            }
        }
        //search for the terms based off String and movie
        Movie movie = null;
        if(!Objects.isNull(request.getId())){
            Optional<Movie> movieOpt = movieRepository.findById(request.getId());
            movie = movieOpt.orElseGet(Movie::new);
            movie.setDescription(request.getDescription());
            movie.setPremiereDate(request.getPremiereDate());
            movie.setTitle(request.getTitle());
        }else{
            movie = new Movie();
            movie.setTitle(request.getTitle());
            movie.setDescription(request.getDescription());
            movie.setPremiereDate(request.getPremiereDate());
            movie.setActive(true);
        }

        Movie savedMovie = movieRepository.save(movie);
        Map<String, MovieTitleToken> existingTokens = this.movieTitleTokenRepository
                .findMovieTitleTokenByMovie_Id(savedMovie.getId())
                .stream()
                .collect(Collectors
                        .toMap(MovieTitleToken::getToken, Function.identity()));

        List<MovieTitleToken> newTokens = wordList.stream().map(word->{
            MovieTitleToken mtt = new MovieTitleToken();
            mtt.setMovie(savedMovie);
            mtt.setToken(word.toLowerCase());
            return mtt;
        }).collect(Collectors.toList());

        newTokens.forEach(newToken->{
            if(existingTokens.get(newToken.getToken()) ==null){
                movieTitleTokenRepository.save(newToken);
            }
        });

        Map<String, MovieTitleToken> newTokensMap = newTokens.stream()
                .collect(Collectors
                        .toMap(MovieTitleToken::getToken, Function.identity()));
        existingTokens.keySet().forEach(existingToken-> {
            if(newTokensMap.get(existingToken) == null){
                movieTitleTokenRepository.delete(existingTokens.get(existingToken));
            }
        });
    }

    public void deactivate(Long id) {
        this.movieRepository.findById(id).ifPresentOrElse(movie -> {
            movie.setActive(false);
            this.movieRepository.save(movie);
        },()->{
            throw new NotFoundException("Movie not found");
        });
    }

    public void rating(Long movieId, RatingRequest request) {
        Movie movie = movieRepository.findActiveMovieById(movieId).orElseThrow(()-> new NotFoundException("Movie not found"));
        TNPrincipal tnPrincipal = UserHelper.currentPrincipal();
        Optional<MovieRating> opt = movieRatingRepository.findByVoterAndMovie( tnPrincipal.getPrincipal(), movie);
        MovieRating mr;
        if (opt.isPresent()){
            mr = opt.get();
            if(!tnPrincipal.hasRole(Roles.ADMIN)){
                throw new ForbiddenException("Principal Not allowed to modify Movie Rating");
            }
        }else{
            mr = new MovieRating();
            mr.setVoter(tnPrincipal.getPrincipal());
        }

        mr.setRating(request.getRating());
        this.movieRatingRepository.save(mr);
    }
    private String[] breakString(String str){
        String[] words = str.split("\\s+");
        return words;
    }

}
