package com.edd.chat.test;

import com.edd.chat.account.Account;
import com.edd.chat.account.AccountRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Creates user accounts used for testing.
 */
@Component
public class AccountFactory {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Username used for test accounts.
     */
    public static final String USERNAME = "username";

    /**
     * Password used for test accounts.
     */
    public static final String PASSWORD = "password";

    /**
     * Account token version.
     */
    public static final UUID VERSION = UUID.randomUUID();

    @Autowired
    public AccountFactory(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder) {

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a valid test account with username of {@value USERNAME} and password {@value PASSWORD}.
     *
     * @return registered test account.
     */
    public Account register() {
        Account account = create(USERNAME)
                .password(passwordEncoder.encode(PASSWORD))
                .tokenVersion(VERSION)
                .enabled()
                .build();

        return accountRepository.save(account);
    }

    /**
     * Create a test account with username of {@value USERNAME} and password {@value PASSWORD}. Note that the password
     * is not encoded.
     *
     * @return test account.
     */
    public static Account create() {
        return create(USERNAME)
                .password(PASSWORD)
                .tokenVersion(VERSION)
                .enabled()
                .build();
    }

    /**
     * Create account builder instance.
     *
     * @param username account username.
     * @return account builder.
     */
    public static AccountBuilder create(String username) {
        return new AccountBuilder(username);
    }

    public static final class AccountBuilder {

        private String username;
        private String password;
        private boolean enabled;
        private Account.Role role = Account.Role.ROLE_USER;
        private UUID tokenVersion = UUID.randomUUID();

        private AccountBuilder(String username) {
            this.username = username;
        }

        public AccountBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AccountBuilder enabled() {
            this.enabled = true;
            return this;
        }

        public AccountBuilder tokenVersion(UUID tokenVersion) {
            this.tokenVersion = tokenVersion;
            return this;
        }

        public AccountBuilder role(Account.Role role) {
            this.role = role;
            return this;
        }

        public Account build() {
            Account account = new Account(username, password,
                    StringUtils.trim(StringUtils.lowerCase(username)));

            // account.setEnabled(enabled);
            account.setTokenVersion(tokenVersion);
            account.setRole(role);
            return account;
        }
    }
}