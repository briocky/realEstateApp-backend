package pl.diminuen.propertysalessystem.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthTokens {
    private String token;
    private String refreshToken;
}
