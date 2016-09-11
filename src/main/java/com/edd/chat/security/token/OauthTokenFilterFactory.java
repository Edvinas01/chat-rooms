package com.edd.chat.security.token;

import com.edd.chat.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

/**
 * Creates oauth token filters.
 */
@Component
public class OauthTokenFilterFactory {

    private final OAuth2ClientContext oauth2ClientContext;
    private final UserManager userManager;

    @Autowired
    public OauthTokenFilterFactory(OAuth2ClientContext oauth2ClientContext,
                                   UserManager userManager) {

        this.oauth2ClientContext = oauth2ClientContext;
        this.userManager = userManager;
    }

    /**
     * Create a filter based on provided resources object.
     *
     * @param resources resources which contain the necessary data to create a filter for a specific provider.
     * @return filter based on provided resource configuration object.
     */
    public Filter create(ClientResources resources) {
        ClientResources.Details details = resources.getDetails();

        OAuth2RestOperations oAuth2RestTemplate =
                new OAuth2RestTemplate(resources.getClient(), oauth2ClientContext);

        OAuth2ClientAuthenticationProcessingFilter processingFilter =
                new OAuth2ClientAuthenticationProcessingFilter(details.getPath());

        processingFilter.setRestTemplate(oAuth2RestTemplate);

        ResourceServerProperties resource = resources.getResource();
        AuthorizationCodeResourceDetails client = resources.getClient();

        // Create a service which will handle authentication requests for this
        // provider type.
        ResourceServerTokenServices tokenServices = new OauthTokenService(
                new FixedPrincipalExtractor(),
                oAuth2RestTemplate,
                new DefaultUsernameResolver(),
                userManager,
                resource.getUserInfoUri(),
                resource.getTokenType(),
                client.getClientId(),
                details.getType());

        processingFilter.setTokenServices(tokenServices);
        return processingFilter;
    }
}