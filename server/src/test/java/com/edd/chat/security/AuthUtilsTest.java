package com.edd.chat.security;

import com.edd.chat.account.Account;
import com.edd.chat.exception.ChatException;
import com.edd.chat.test.AccountFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthUtilsTest {

    private Account account;

    @Before
    public void setUp() {
        account = AccountFactory
                .create("CoolGuy")
                .build();
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test(expected = ChatException.class)
    public void getUsernameNoAuthentication() {
        AuthUtils.getUsername();
    }

    @Test
    public void getUsername() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities()));

        assertThat(AuthUtils.getUsername())
                .isEqualTo(account.getInternalUsername());
    }
}