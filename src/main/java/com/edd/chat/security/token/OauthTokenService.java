package com.edd.chat.security.token;

import com.edd.chat.domain.user.ChatUser;
import com.edd.chat.user.UserManager;
import com.edd.chat.user.UsernameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Map;

public class OauthTokenService implements ResourceServerTokenServices {

    private static final Logger LOGGER = LoggerFactory.getLogger(OauthTokenService.class);

    private final PrincipalExtractor principalExtractor;
    private final OAuth2RestOperations restOperations;
    private final UsernameResolver usernameResolver;
    private final UserManager userManager;
    private final String userInfoUrl;
    private final String tokenType;
    private final String clientId;
    private final String type;

    public OauthTokenService(PrincipalExtractor principalExtractor,
                             OAuth2RestOperations restOperations,
                             UsernameResolver usernameResolver,
                             UserManager userManager,
                             String userInfoUrl,
                             String tokenType,
                             String clientId,
                             String type) {

        this.principalExtractor = principalExtractor;
        this.restOperations = restOperations;
        this.usernameResolver = usernameResolver;
        this.userManager = userManager;
        this.userInfoUrl = userInfoUrl;
        this.tokenType = tokenType;
        this.clientId = clientId;
        this.type = type;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {

        Map<String, Object> details = getMap(accessToken);

        // How will we identify the user against this provider type.
        String identifier = principalExtractor
                .extractPrincipal(details)
                .toString();

        // Get existing user details or create a new user.
        ChatUser user = userManager.getUser(usernameResolver
                .resolve(details), identifier, type);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());

        authentication.setDetails(details);

        return new OAuth2Authentication(
                new OAuth2Request(null, clientId, null, true, null, null, null, null, null),
                authentication);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Reading of access token is not supported");
    }

    /**
     * Get map with authentication details.
     */
    private Map<String, Object> getMap(String accessToken) {
        LOGGER.debug("Getting user info from: {}, type: {}",
                userInfoUrl, type);

        try {
            OAuth2AccessToken existingToken = restOperations
                    .getOAuth2ClientContext()
                    .getAccessToken();

            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(
                        accessToken);

                token.setTokenType(this.tokenType);
                restOperations.getOAuth2ClientContext().setAccessToken(token);
            }

            // return restTemplate.getForEntity(path, Map.class).getBody();
            return restOperations
                    .exchange(userInfoUrl,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            })
                    .getBody();

        } catch (Exception e) {
            LOGGER.error("Could not fetch user details: {}, {}",
                    e.getClass(), e.getMessage());

            throw new InvalidTokenException(accessToken);
        }
    }
}