package com.movie.movieapp.controller.movie;

import com.movie.movieapp.entity.Movie;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate premiereDate;
    private ZonedDateTime createdAt;
    private String s3Link;
    private String plot;
    private String language;
    private String crew;

    public MovieDTO(Movie movie){
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.premiereDate = movie.getPremiereDate();
        this.createdAt = movie.getCreatedAt();
        this.plot = movie.getPlot();
        this.language = movie.getLanguage();
        this.crew = movie.getCrew();
    }

    public MovieDTO(Movie movie,String bucket){
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.premiereDate = movie.getPremiereDate();
        this.createdAt = movie.getCreatedAt();
        this.plot = movie.getPlot();
        this.language = movie.getLanguage();
        this.crew = movie.getCrew();
        if(movie.getS3PicturePath()!=null){
            this.s3Link = String.format("https://%s.s3.amazonaws.com/%s",bucket,movie.getS3PicturePath());
        }
    }
    public Movie toMovie(){
        Movie movie = new Movie();
        movie.setTitle(getTitle());
        movie.setDescription(getDescription());
        movie.setPremiereDate(getPremiereDate());
        return movie;
    }
}
