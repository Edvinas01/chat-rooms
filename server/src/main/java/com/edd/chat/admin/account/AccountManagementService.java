package com.edd.chat.admin.account;

import com.edd.chat.account.Account;
import com.edd.chat.account.AccountLookup;
import com.edd.chat.account.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagementService.class);

    private final AccountRepository accountRepository;
    private final AccountLookup accountLookup;

    @Autowired
    public AccountManagementService(AccountRepository accountRepository,
                                    AccountLookup accountLookup) {

        this.accountRepository = accountRepository;
        this.accountLookup = accountLookup;
    }

    /**
     * Manage other users account.
     *
     * @param id      account id.
     * @param details details to set to the account.
     */
    public Account updateAccount(String id,
                                 AccountManagementDetails details) {

        Account account = accountLookup.getAccount(id);
        account.setEnabled(details.isEnabled());

        LOGGER.debug("Setting enabled to: {} of account: {}",
                details.isEnabled(), account.getInternalUsername());

        return accountRepository.save(account);
    }
}