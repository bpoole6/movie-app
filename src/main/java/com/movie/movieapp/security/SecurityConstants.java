package com.movie.movieapp.security;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    //For safety concerns Admin roles should have a shorter TTL than regular non-users incase of JWT theft.
    public static final long ADMIN_EXPIRATION_TIME = 7200000; // 2 hour
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
}
