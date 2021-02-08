package com.movie.movieapp;

import com.movie.movieapp.entity.Movie;
import com.movie.movieapp.entity.Principal;
import com.movie.movieapp.repo.MovieRepository;
import com.movie.movieapp.repo.PrincipalRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.ZonedDateTime;
import java.util.*;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MovieAppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(MovieAppApplication.class, args);

    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        String storeKey = Optional.ofNullable(System.getProperty("storeKey")).orElseThrow(() -> new RuntimeException("Missing storeKey"));
        Properties props = new Configuration().getStoreProperties(storeKey);
        System.out.println("------- +++++++++++++++++++++++++++++++ -------- ");
        System.out.println("------- +++ Using storeKey prop =" + storeKey);
        System.out.println("------- +++++++++++++++++++++++++++++++ -------- ");

        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setProperties(props);
        pspc.setIgnoreUnresolvablePlaceholders(true);
        return pspc;

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean // Makes ZonedDateTime compatible with auditing fields
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }

}
