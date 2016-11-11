package com.edd.chat.account;

import com.edd.chat.exception.ChatException;
import com.edd.chat.security.AuthUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService implements AccountLookup {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder) {

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get currently authenticated account.
     *
     * @return account details.
     */
    public Account getAccount() {
        return accountRepository
                .findByInternalUsername(AuthUtils.getUsername())
                .orElseThrow(() -> new ChatException("Not authenticated", HttpStatus.UNAUTHORIZED));
    }

    /**
     * Register a new account.
     *
     * @param details account registration details.
     * @return registered account details.
     */
    public Account register(Account details) {
        if (StringUtils.isAnyBlank(details.getUsername(),
                details.getPassword())) {

            throw new ChatException("Password and username must not be blank", HttpStatus.BAD_REQUEST);
        }

        String username = details.getUsername().trim();
        String internalUsername = username.toLowerCase(Locale.getDefault());

        Account account = new Account(
                username,
                passwordEncoder.encode(details.getPassword()),
                internalUsername);

        account.setRole(Account.Role.ROLE_USER);

        LOGGER.debug("Registering new account with username: {}", internalUsername);

        try {
            return accountRepository.save(account);
        } catch (DuplicateKeyException e) {
            LOGGER.error("Could not create account: {}",
                    e.getMostSpecificCause().getMessage());

            throw new ChatException("Invalid username", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Changes currently authenticated accounts token version to a new one.
     */
    public void logout() {
        Account account = getAccount();
        account.setTokenVersion(UUID.randomUUID());
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccount(String id) {
        return Optional.ofNullable(accountRepository.findOne(id))
                .orElseThrow(() -> new ChatException("Account does not exist", HttpStatus.NOT_FOUND));
    }
}