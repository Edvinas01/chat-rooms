package com.edd.chat.security;

import com.edd.chat.token.TokenHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Creates authentication from json web tokens.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final TokenHandler tokenHandler;

    public TokenAuthenticationFilter(UserDetailsService userDetailsService,
                                     TokenHandler tokenHandler) {

        this.userDetailsService = userDetailsService;
        this.tokenHandler = tokenHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            tokenHandler
                    .parseUsername(request.getHeader(HttpHeaders.AUTHORIZATION))
                    .map(userDetailsService::loadUserByUsername)
                    // If token is parsed and such user exists, create authentication.
                    .ifPresent(u -> {
                        // todo need mechanism to enable/disable accounts.
//                        if (!u.isEnabled()) {
//                            return;
//                        }

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });

        } catch (UsernameNotFoundException ignored) {
        }

        filterChain.doFilter(request, response);
    }
}