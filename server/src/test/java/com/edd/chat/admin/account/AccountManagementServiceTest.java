package com.edd.chat.admin.account;

import com.edd.chat.account.Account;
import com.edd.chat.account.AccountLookup;
import com.edd.chat.account.AccountRepository;
import com.edd.chat.test.AccountFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountManagementServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountLookup accountLookup;

    private AccountManagementService service;
    private Account account;

    @Before
    public void setUp() {
        service = new AccountManagementService(accountRepository, accountLookup);
        account = AccountFactory
                .create(AccountFactory.USERNAME)
                .build();

        when(accountRepository.save(any(Account.class)))
                .then(returnsFirstArg());
    }

    @Test
    public void updateAccount() {
        when(accountLookup.getAccount(any(String.class)))
                .thenReturn(account);

        account = service.updateAccount(account.getId(),
                new AccountManagementDetails(true));

        assertThat(account.isEnabled()).isTrue();
    }
}