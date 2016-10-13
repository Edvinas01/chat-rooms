package com.edd.chat.token;

import com.edd.chat.account.Account;
import com.edd.chat.exception.ChatException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Helps jwt handling.
 */
@Component
public class TokenHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenHandler.class);
    private static final String VERSION = "version";
    private static final String BEARER = "Bearer";

    private final String secret;
    private final int expiration;

    @Autowired
    public TokenHandler(@Value("${chat.auth.token.secret}") String secret,
                        @Value("${chat.auth.token.expiration}") int expiration) {

        this.secret = secret;
        this.expiration = expiration;
    }

    /**
     * Create token from given account details.
     *
     * @param account account whose details to use.
     * @return token with some additional details added.
     */
    public TokenDetails createToken(Account account) {
        Date expires = DateTime
                .now()
                .plusHours(expiration)
                .toDate();

        Claims claims = Jwts.claims()
                .setIssuedAt(new Date())
                .setExpiration(expires)
                .setSubject(account.getInternalUsername());

        claims.put(VERSION, account.getTokenVersion());

        String value = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return new TokenDetails(value, expires);
    }

    /**
     * Parse token details from provided jwt.
     *
     * @param rawToken raw jwt, can contain {@value BEARER} at the start of it.
     * @return decoded token details optional.
     */
    public Optional<DecodedToken> parse(String rawToken) {
        if (StringUtils.isBlank(rawToken)) {
            return Optional.empty();
        }

        String token = rawToken
                .replaceFirst(BEARER, "");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            String version = Optional
                    .ofNullable(claims.get(VERSION, String.class))
                    .orElseThrow(() -> new ChatException("Token must include version", HttpStatus.BAD_REQUEST));

            return Optional.of(new DecodedToken(claims.getSubject(), UUID.fromString(version)));

        } catch (JwtException e) {
            LOGGER.error("Could not parse token: {}", token, e);

            return Optional.empty();
        }
    }
}