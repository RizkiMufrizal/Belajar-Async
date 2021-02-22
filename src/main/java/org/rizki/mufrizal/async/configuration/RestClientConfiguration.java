package org.rizki.mufrizal.async.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableOAuth2Client
public class RestClientConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    protected OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
        ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
        clientCredentialsResourceDetails.setClientId(environment.getRequiredProperty("security.oauth2.client.clientId"));
        clientCredentialsResourceDetails.setClientSecret(environment.getRequiredProperty("security.oauth2.client.clientSecret"));
        clientCredentialsResourceDetails.setAccessTokenUri(environment.getRequiredProperty("security.oauth2.client.accessTokenUri"));
        clientCredentialsResourceDetails.setGrantType(environment.getRequiredProperty("security.oauth2.client.grantType"));
        return clientCredentialsResourceDetails;
    }

    @Bean
    public OAuth2RestOperations oAuth2RestOperations() {
        return new OAuth2RestTemplate(oAuth2ProtectedResourceDetails(), new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));
    }

}