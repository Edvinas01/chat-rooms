package com.edd.chat.token;

import com.edd.chat.account.AccountService;
import com.edd.chat.account.Account;
import com.edd.chat.account.AccountRepository;
import com.edd.chat.exception.ChatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TokenServiceIntegrationTest {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AccountService accountService;
    @Resource
    private TokenHandler tokenHandler;

    private TokenService tokenService;
    private Credentials credentials;

    @Before
    public void setUp() {
        credentials = new Credentials("test", "password");
        tokenService = new TokenService(accountRepository, passwordEncoder, tokenHandler);

        accountService.register(new Account(
                credentials.getUsername(),
                credentials.getPassword(),
                null));
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    public void createToken() {
        Token token = tokenService.createToken(credentials);
        assertThat(token.getExpires()).isNotNull();
        assertThat(token.getToken()).isNotNull();
    }

    @Test
    public void invalidPassword() {
        assertThatThrownBy(() -> tokenService
                .createToken(new Credentials(credentials.getUsername(), "")))
                .isExactlyInstanceOf(ChatException.class);
    }

    @Test
    public void invalidCredentials() {
        assertThatThrownBy(() -> tokenService
                .createToken(new Credentials("", "")))
                .isExactlyInstanceOf(ChatException.class);
    }
}