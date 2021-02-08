package com.movie.movieapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SpringSecurityConfig( BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // Create 2 users for demo
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("admin").password("secretPassword").roles("USER", "ADMIN")
                .and().withUser("user1").password("secretPassword")
                .and().withUser("user2").password("secretPassword");

    }
    //keytool -export -keystore cert.p12 -alias cert -file cert.crt

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.requiresChannel().anyRequest().requiresSecure().and()
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/*/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().defaultSuccessUrl("/");
    }


}
