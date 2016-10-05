package com.edd.chat.config;

import com.edd.chat.security.TokenAuthenticationEntryPoint;
import com.edd.chat.security.TokenAuthenticationFilter;
import com.edd.chat.token.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationEntryPoint entryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenHandler tokenHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers(
                        "/api/v1/accounts/authenticate",
                        "/api/v1/accounts/register")
                    .permitAll()
                .anyRequest()
                    .authenticated();

        http.addFilterBefore(tokenAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);

        http.headers()
                .cacheControl();
        // @formatter:on
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        return new TokenAuthenticationFilter(userDetailsService, tokenHandler);
    }
}