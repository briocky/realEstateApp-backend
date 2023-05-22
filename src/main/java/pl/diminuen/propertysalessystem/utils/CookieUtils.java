package pl.diminuen.propertysalessystem.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    @Value("${app.jwt.refreshToken.expiration.milis}")
    private int refreshTokenExpMilis;

    public Cookie buildRefreshTokenCookie(String refreshToken) {
        final String refreshTokenCookieName = "refreshToken";
        final int maxRefreshTokenCookieAge = refreshTokenExpMilis / 1000;
        final String rootPath = "/";

        Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, refreshToken);
        refreshTokenCookie.setMaxAge(maxRefreshTokenCookieAge);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath(rootPath);

        return refreshTokenCookie;
    }

    public Cookie buildAuthTokenCookie(String token) {
        final String tokenCookieName = "token";
        final int maxAuthTokenCookieAge = 240;
        final String rootPath = "/";

        Cookie refreshTokenCookie = new Cookie(tokenCookieName, token);
        refreshTokenCookie.setMaxAge(maxAuthTokenCookieAge);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath(rootPath);

        return refreshTokenCookie;
    }
}
