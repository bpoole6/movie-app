package com.movie.movieapp.controller.movie;

import com.movie.movieapp.entity.Movie;
import com.movie.movieapp.entity.MovieRating;
import com.movie.movieapp.util.TimeUtils;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Optional;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private String premiereDate;
    private ZonedDateTime createdAt;
    private String s3Link;
    private String plot;
    private String language;
    private String crew;
    private double rating;
    private Integer userRating;
    public MovieDTO(Movie movie){
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.premiereDate = TimeUtils.ZONED_DATE.format(movie.getPremiereDate());
        this.createdAt = movie.getCreatedAt();
        this.plot = movie.getPlot();
        this.language = movie.getLanguage();
        this.crew = movie.getCrew();
    }

    public MovieDTO(Movie movie,String bucket, double percent, MovieRating movieRating){
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.premiereDate = TimeUtils.ZONED_DATE.format(movie.getPremiereDate());
        this.createdAt = movie.getCreatedAt();
        this.plot = movie.getPlot();
        this.language = movie.getLanguage();
        this.crew = movie.getCrew();
        if(movie.getS3PicturePath()!=null){
            this.s3Link = String.format("https://%s.s3.amazonaws.com/%s",bucket,movie.getS3PicturePath());
        }
        if(!Double.isNaN(percent))
        this.rating = percent;
        Optional.ofNullable(movieRating).ifPresent(r->this.setUserRating(r.getRating().getRating()));
    }
}
