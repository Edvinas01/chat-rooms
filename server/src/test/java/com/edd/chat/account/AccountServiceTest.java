package com.edd.chat.account;

import com.edd.chat.exception.ChatException;
import com.edd.chat.test.AccountFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.edd.chat.test.AccountFactory.PASSWORD;
import static com.edd.chat.test.AccountFactory.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AccountService service;
    private Account account;

    @Before
    public void setUp() {
        service = new AccountService(accountRepository, passwordEncoder);

        account = AccountFactory.create();

        // Mocking password encoding.
        when(passwordEncoder.encode(anyString())).then(a -> {
            String password = a.getArgumentAt(0, String.class);
            return password + password;
        });

        when(accountRepository.findByInternalUsername(account.getInternalUsername()))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .then(returnsFirstArg());

        when(passwordEncoder.matches(PASSWORD, PASSWORD))
                .thenReturn(true);
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getAccount() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities()));

        assertThat(service.getAccount())
                .isEqualTo(account);
    }

    @Test(expected = ChatException.class)
    public void getAccountNotAuthenticated() {
        service.getAccount();
    }

    @Test
    public void register() {
        Account account = AccountFactory
                .create(USERNAME + "other")
                .password(PASSWORD)
                .role(null)
                .build();

        Account registered = service.register(account);
        assertThat(registered.getUsername()).isEqualTo(account.getUsername());
        assertThat(registered.getPassword()).isNotEqualTo(account.getPassword());
        assertThat(registered.getRole()).isEqualTo(Account.Role.ROLE_USER);
        assertThat(registered.getInternalUsername()).isEqualTo(account
                .getUsername()
                .trim()
                .toLowerCase());
    }

    @Test(expected = ChatException.class)
    public void registerBlankUsername() {
        Account account = AccountFactory
                .create("")
                .build();

        service.register(account);
    }

    @Test(expected = ChatException.class)
    public void registerBlankPassword() {
        Account account = AccountFactory
                .create(USERNAME)
                .password("")
                .build();

        service.register(account);
    }
}