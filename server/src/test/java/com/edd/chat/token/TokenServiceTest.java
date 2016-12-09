package com.edd.chat.token;

import com.edd.chat.account.Account;
import com.edd.chat.account.AccountRepository;
import com.edd.chat.exception.ChatException;
import com.edd.chat.test.AccountFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.edd.chat.test.AccountFactory.PASSWORD;
import static com.edd.chat.test.AccountFactory.USERNAME;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    private static final String SECRET = "secret";
    private static final int EXPIRES = 1;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Credentials credentials;
    private TokenService service;

    @Before
    public void setUp() {
        String encodedPassword = PASSWORD + PASSWORD;

        Account account = AccountFactory
                .create(USERNAME)
                .password(encodedPassword)
                .enabled()
                .build();

        service = new TokenService(accountRepository,
                passwordEncoder,
                new TokenHandler(SECRET, EXPIRES));

        credentials = new Credentials(account.getUsername(), PASSWORD);

        when(accountRepository.findByInternalUsername(any(String.class)))
                .thenReturn(Optional.empty());

        when(accountRepository.findByInternalUsername(USERNAME))
                .thenReturn(Optional.of(account));

        when(passwordEncoder.matches(any(String.class), any(String.class))).then(a -> {
            String raw = a.getArgumentAt(0, String.class);
            String encoded = a.getArgumentAt(1, String.class);

            // Mock encoding.
            return encoded.equals(raw + raw);
        });
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    public void createToken() {
        TokenDetails token = service.createToken(credentials);
        assertThat(token.getExpires()).isNotNull();
        assertThat(token.getToken()).isNotNull();
    }

    @Test(expected = ChatException.class)
    public void createTokenInvalidPassword() {
        service.createToken(new Credentials(credentials.getUsername(), PASSWORD + "bad"));
    }

    @Test(expected = ChatException.class)
    public void createTokenInvalidCredentials() {
        service.createToken(new Credentials("", ""));
    }

    @Test(expected = ChatException.class)
    public void createTokenAccountDoesNotExist() {
        service.createToken(new Credentials(credentials.getUsername() + "bad", PASSWORD));
    }
}