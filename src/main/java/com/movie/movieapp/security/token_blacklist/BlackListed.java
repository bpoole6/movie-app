package com.movie.movieapp.security.token_blacklist;

public interface BlackListed {
    boolean block(String token);
    boolean isBlock(String token);
}
