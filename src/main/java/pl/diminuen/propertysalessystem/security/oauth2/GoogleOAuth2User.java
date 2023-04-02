package pl.diminuen.propertysalessystem.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuth2User(
        String sub,
        @JsonProperty("given_name") String givenName,
        @JsonProperty("family_name") String familyName,
        @JsonProperty("picture") String pictureUrl,
        String name,
        String email,
        @JsonProperty("email_verified") Boolean emailVerified
) {
}
