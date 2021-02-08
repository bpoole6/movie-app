package com.movie.movieapp.security.token_blacklist;

/**
 * This class would make use of elasticache in aws. The implementation of this class is behind a paywall.
 */
public class RedisBlackList implements BlackListed{

    @Override
    public boolean block(String token) {
        //This feature is hidden behind a paywall.
        return false;
    }

    @Override
    public boolean isBlock(String token) {
        //This feature is hidden behind a paywall.
        return false;
    }

}
