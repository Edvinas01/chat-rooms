package com.edd.chat.account;

import com.edd.chat.domain.account.Account;
import com.edd.chat.domain.account.AccountRepository;
import com.edd.chat.exception.ChatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceIntegrationTest {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountService(accountRepository, passwordEncoder);
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void nullUsername() {
        accountRepository.save(new Account(null, null, ""));
    }

    @Test
    public void registerAccount() {
        Account account = new Account(" TEST ", "password", null);
        Account registered = accountService.register(account);

        assertThat(registered.getId()).isNotNull();
        assertThat(registered.isEnabled()).isFalse();
        assertThat(registered.getRole()).isEqualTo(Account.Role.ROLE_USER);
        assertThat(registered.getPassword()).isNotEqualTo(account.getPassword());
        assertThat(registered.getUsername()).isEqualTo(account.getUsername().trim());
        assertThat(registered.getInternalUsername()).isEqualToIgnoringCase(account.getUsername().trim());
    }

    @Test
    public void accountExists() {
        Account account = new Account(" TEST ", "password", null);
        accountService.register(account);

        assertThatThrownBy(() -> accountService.register(account))
                .isInstanceOf(ChatException.class);
    }

    @Test
    public void getAuthenticatedAccount() {
        Account account = accountService
                .register(new Account("test", "password", null));

        SecurityContextHolder
                .getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities()));

        assertThat(accountService.getAccount().getId())
                .isEqualTo(account.getId());
    }

    @Test
    public void invalidRegistrationCredentials() {
        assertThatThrownBy(() -> accountService.register(new Account("", "", null)))
                .isInstanceOf(ChatException.class);
    }
}