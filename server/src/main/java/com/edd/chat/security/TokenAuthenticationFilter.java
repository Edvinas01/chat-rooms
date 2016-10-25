package com.edd.chat.security;

import com.edd.chat.account.Account;
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
                    .parse(request.getHeader(HttpHeaders.AUTHORIZATION))
                    .map(t -> {
                        Account account = (Account) userDetailsService
                                .loadUserByUsername(t.getUsername());

                        // When user logs out the token version should change,
                        // so old tokens become invalid.
                        if (account.getTokenVersion().equals(t.getVersion())) {
                            return account;
                        }
                        return null;
                    })
                    // If token is parsed and such user exists, create authentication.
                    .ifPresent(u -> {
                        if (!u.isEnabled()) { // todo test for this, also error is in wrong format
                            return;
                        }
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });

        } catch (UsernameNotFoundException ignored) {
        }

        filterChain.doFilter(request, response);
    }
}