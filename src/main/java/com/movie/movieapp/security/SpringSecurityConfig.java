package com.movie.movieapp.security;

import com.movie.movieapp.security.token_blacklist.BlackListed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    private final AuthenticationProvider authenticationProvider;
    private final EncryptedKeysService encryptedKeysService;
    private  final BlackListed blackListed;
    private  final CorsFilter corsFilter;
    @Autowired
    public SpringSecurityConfig(AuthenticationProvider authenticationProvider, EncryptedKeysService encryptedKeysService, BlackListed blackListed, CorsFilter corsFilter) {
        super();
        this.authenticationProvider = authenticationProvider;
        this.encryptedKeysService = encryptedKeysService;
        this.blackListed = blackListed;
        this.corsFilter = corsFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilter(corsFilter).cors().and().csrf().disable().authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), encryptedKeysService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), encryptedKeysService, blackListed))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ADMIN>USER");
        return roleHierarchy;
    }
}
