package com.edd.chat.security.token;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class ClientResources {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();

    @NestedConfigurationProperty
    private Details details = new Details();

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }

    public Details getDetails() {
        return details;
    }

    public static class Details {

        private String path;
        private String type;

        /**
         * Get provider type name.
         *
         * @return provider type name.
         */
        public String getType() {
            return type;
        }

        /**
         * Get login path.
         *
         * @return login path.
         */
        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}