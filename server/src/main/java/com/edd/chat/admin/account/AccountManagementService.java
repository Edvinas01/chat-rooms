package com.edd.chat.admin.account;

import com.edd.chat.account.Account;
import com.edd.chat.account.AccountRepository;
import com.edd.chat.exception.ChatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountManagementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagementService.class);

    private final AccountRepository accountRepository;

    @Autowired
    public AccountManagementService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Get list of registered account.
     *
     * @return list of registered accounts.
     */
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Manage other users account.
     *
     * @param id      account id.
     * @param details details to set to the account.
     */
    public Account updateAccount(String id,
                                 AccountManagementDetails details) {

        Account account = getAccount(id);
        account.setEnabled(details.isEnabled());

        LOGGER.debug("Setting enabled to: {} of account: {}",
                details.isEnabled(), account.getInternalUsername());

        return accountRepository.save(account);
    }

    /**
     * Get account by id by ignoring authentication.
     *
     * @param id account id.
     * @return account.
     */
    public Account getAccount(String id) {
        return Optional.ofNullable(accountRepository.findOne(id))
                .orElseThrow(() -> new ChatException("Account does not exist", HttpStatus.NOT_FOUND));
    }
}