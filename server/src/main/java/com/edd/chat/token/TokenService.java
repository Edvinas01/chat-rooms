package com.edd.chat.token;

import com.edd.chat.account.Account;
import com.edd.chat.account.AccountRepository;
import com.edd.chat.exception.ChatException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Creates authentication tokens for accounts.
 */
@Service
public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHandler tokenHandler;

    @Autowired
    public TokenService(AccountRepository accountRepository,
                        PasswordEncoder passwordEncoder,
                        TokenHandler tokenHandler) {

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenHandler = tokenHandler;
    }

    /**
     * Create token from provided credentials.
     *
     * @param credentials credentials used when creating token.
     * @return jwt with additional details.
     */
    public TokenDetails createToken(Credentials credentials) {
        if (StringUtils.isAnyBlank(credentials.getPassword(),
                credentials.getUsername())) {

            throw new ChatException("Password and username must not be empty", HttpStatus.BAD_REQUEST);
        }

        String username = credentials
                .getUsername()
                .trim()
                .toLowerCase(Locale.getDefault());

        Account account = accountRepository
                .findByInternalUsername(username)
                .orElseThrow(() -> new ChatException("Invalid credentials", HttpStatus.UNAUTHORIZED));

        if (!account.isEnabled()) {
            throw new ChatException("Account is not verified", HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(credentials.getPassword(),
                account.getPassword())) {

            throw new ChatException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        LOGGER.debug("Creating token for account with internal username: {}", username);
        return tokenHandler.createToken(account);
    }
}