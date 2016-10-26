package com.edd.chat.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationTest {

    private static final String ERROR = "bad";
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void accessDeniedHandlerHandle() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        new TokenAccessDeniedHandler(mapper).handle(
                new MockHttpServletRequest(),
                response,
                new AccessDeniedException(ERROR));

        assertError(response);
    }

    @Test
    public void authenticationEntryPointCommence() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        new TokenAuthenticationEntryPoint(mapper).commence(
                new MockHttpServletRequest(),
                response,
                new UsernameNotFoundException(ERROR));

        assertError(response);
    }

    /**
     * Assert error structure returned from servlet response.
     */
    private void assertError(MockHttpServletResponse response) throws Exception {
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);

        String error = JsonPath.read(response.getContentAsString(), "$.error");
        assertThat(error).isEqualTo(ERROR);
    }
}