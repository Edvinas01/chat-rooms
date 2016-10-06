package com.edd.chat.account;

import com.edd.chat.domain.account.Account;
import com.edd.chat.exception.ChatException;
import com.edd.chat.exception.GlobalExceptionHandler;
import com.edd.chat.token.Credentials;
import com.edd.chat.token.Token;
import com.edd.chat.token.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TokenService tokenService;

    private ObjectMapper objectMapper;
    private Account account;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        account = new Account("test", "password", null);
        account.setRole(Account.Role.ROLE_USER);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new AccountController(accountService, tokenService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void authenticate() throws Exception {
        Token token = new Token("abc", new Date());

        when(tokenService.createToken(any(Credentials.class)))
                .thenReturn(token);

        mockMvc.perform(post("/api/v1/accounts/authenticate")
                .content(objectMapper.writeValueAsString(new Credentials("test", "password")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token.getToken()))
                .andExpect(jsonPath("$.expires").value(token.getExpires().getTime()));
    }

    @Test
    public void registerAccount() throws Exception {
        AccountModel model = new AccountModel(
                account.getUsername(),
                account.getPassword(),
                null, null);

        when(accountService.register(any(Account.class)))
                .thenReturn(account);

        mockMvc.perform(post("/api/v1/accounts/register")
                .content(objectMapper.writeValueAsString(model))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.role").value(account.getRole().name()))
                .andExpect(jsonPath("$.username").value(account.getUsername()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void invalidRegistrationCredentials() throws Exception {
        HttpStatus code = HttpStatus.BAD_REQUEST;
        String message = "bad";

        when(accountService.register(any(Account.class)))
                .thenThrow(new ChatException(message, code));

        mockMvc.perform(post("/api/v1/accounts/register")
                .content(objectMapper.writeValueAsString(new AccountModel(null, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(code.value()))
                .andExpect(jsonPath("$.error").value(message));
    }
}