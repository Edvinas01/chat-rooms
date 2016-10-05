package com.edd.chat.security;

import com.edd.chat.domain.account.Account;
import com.edd.chat.exception.ChatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AuthUtilsTest {

    private static final String ID = "abc";
    private Account account;

    @Before
    public void setUp() {
        account = Mockito.mock(Account.class);
        when(account.getId()).thenReturn(ID);
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test(expected = ChatException.class)
    public void noAuthentication() {
        AuthUtils.getId();
    }

    @Test
    public void getId() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities()));

        assertThat(AuthUtils.getId()).isEqualTo(ID);
    }
}