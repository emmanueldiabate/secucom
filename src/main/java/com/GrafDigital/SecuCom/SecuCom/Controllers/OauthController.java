package com.GrafDigital.SecuCom.SecuCom.Controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@AllArgsConstructor
public class OauthController {
    private static final Logger log = LoggerFactory.getLogger(OauthController.class);
    private OAuth2AuthorizedClientService loadAuthorizedClientService;

    @RequestMapping("/**")

    private StringBuffer getOauth2LoginInfo(Principal user) {
        System.err.println(user + " Notre user connecter avec succès !");
        StringBuffer protectedInfo = new StringBuffer();
        OAuth2User principal = ((OAuth2AuthenticationToken) user).getPrincipal();
        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
        OAuth2AuthorizedClient authClient = this.loadAuthorizedClientService
                .loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
        if (authToken.isAuthenticated()) {

            Map<String, Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

            String userToken = authClient.getAccessToken().getTokenValue();
            protectedInfo.append("Bienvenue, " + userAttributes.get("name") + "<br><br>");
            protectedInfo.append("e-mail: " + userAttributes.get("email") + "<br><br>");
            protectedInfo.append("Access Token: " + userToken + "<br><br>");
            OidcIdToken idToken = getIdToken(principal);
            if (idToken != null) {

                protectedInfo.append("idToken value: " + idToken.getTokenValue() + "<br><br>");
                protectedInfo.append("Token mapped values <br><br>");

                Map<String, Object> claims = idToken.getClaims();

                for (String key : claims.keySet()) {
                    protectedInfo.append("  " + key + ": " + claims.get(key) + "<br>");
                }
            }
        } else {
            protectedInfo.append("NA");
        }
        return protectedInfo;
    }

    private OidcIdToken getIdToken(OAuth2User principal) {
        log.info("User connecter avec google");
        if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            return oidcUser.getIdToken();
        }
        return null;
    }
}
