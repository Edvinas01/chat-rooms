package com.edd.chat.token;

import com.edd.chat.account.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TokenHandlerTest {

    private static final String USERNAME = "test";

    @Mock
    private Account account;

    private TokenHandler tokenHandler;

    @Before
    public void setUp() {
        tokenHandler = new TokenHandler("secret", 1);
        when(account.getInternalUsername()).thenReturn(USERNAME);
    }

    @Test
    public void createTokenAndParseUsername() {
        String token = tokenHandler.createToken(account)
                .getToken();

        assertThat(tokenHandler.parseUsername(token).get())
                .isEqualTo(USERNAME);
    }

    @Test
    public void unableToParseUsername() {
        assertThat(tokenHandler.parseUsername("abc")).isEmpty();
    }
}