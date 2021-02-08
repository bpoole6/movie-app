package com.movie.movieapp.security.token_blacklist;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

@Component
public class InMemoryBlackList implements BlackListed {
    private final Cache  <String, Boolean> cache;
    public InMemoryBlackList(){
         cache = CacheBuilder.newBuilder()
                .maximumSize(Long.MAX_VALUE)
                .build(); // look Ma, no CacheLoader
    }
    @Override
    public boolean block(String token) {
        try{
            cache.put(token,true);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean isBlock(String token) {
        return Boolean.TRUE.equals(cache.getIfPresent(token));
    }
}
