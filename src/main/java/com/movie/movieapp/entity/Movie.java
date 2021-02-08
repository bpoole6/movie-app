package com.movie.movieapp.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "movie")
public class Movie extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    private String description;
    private LocalDate premiereDate;
    private boolean active = true;
    @Column(name = "s3_picture_path")
    private String s3PicturePath;
    private String plot;
    private String language;
    private String crew;

}
