package com.edd.chat.token;

import com.edd.chat.account.Account;
import com.edd.chat.exception.ChatException;
import com.edd.chat.test.AccountFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.edd.chat.test.AccountFactory.USERNAME;
import static com.edd.chat.test.AccountFactory.VERSION;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TokenHandlerTest {

    private static final String SECRET = "secret";
    private static final int EXPIRATION = 1;

    private TokenHandler tokenHandler;
    private Account account;

    @Before
    public void setUp() {
        tokenHandler = new TokenHandler(SECRET, EXPIRATION);
        account = AccountFactory.create();
    }

    @Test
    public void createTokenAndParseUsername() {
        String token = tokenHandler
                .createToken(account)
                .getToken();

        DecodedToken decoded = tokenHandler.parse("Bearer " + token).get();
        assertThat(decoded.getUsername()).isEqualTo(USERNAME);
        assertThat(decoded.getVersion()).isEqualTo(VERSION);
    }

    @Test
    public void parseUsernameInvalidToken() {
        assertThat(tokenHandler.parse("abc")).isEmpty();
    }

    @Test(expected = ChatException.class)
    public void parseTokenWithoutVersion() {
        account.setTokenVersion(null);
        tokenHandler.parse(tokenHandler.createToken(account).getToken());
    }
}