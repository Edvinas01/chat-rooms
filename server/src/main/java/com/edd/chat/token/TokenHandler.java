package com.edd.chat.token;

import com.edd.chat.domain.account.Account;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * Helps jwt handling.
 */
@Component
public class TokenHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenHandler.class);
    private static final String BEARER = "Bearer";

    private final String secret;
    private final int expiration;

    @Autowired
    public TokenHandler(@Value("${auth.token.secret}") String secret,
                        @Value("${auth.token.expiration}") int expiration) {

        this.secret = secret;
        this.expiration = expiration;
    }

    /**
     * Create token from given account details.
     *
     * @param account account whose details to use.
     * @return token with some additional details added.
     */
    public Token createToken(Account account) {
        Date expires = DateTime
                .now()
                .plusHours(expiration)
                .toDate();

        String value = Jwts.builder()
                .setSubject(account.getInternalUsername())
                .setExpiration(expires)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return new Token(value, expires);
    }

    /**
     * Parse internal username from provided jwt.
     *
     * @param rawToken raw jwt, can contain {@value BEARER} at the start of it.
     * @return internal username optional.
     */
    public Optional<String> parseUsername(String rawToken) {
        if (StringUtils.isBlank(rawToken)) {
            return Optional.empty();
        }

        String token = rawToken
                .replaceFirst(BEARER, "");

        try {
            return Optional.of(Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());

        } catch (JwtException e) {
            LOGGER.error("Could not parse token: {}", token, e);

            return Optional.empty();
        }
    }
}