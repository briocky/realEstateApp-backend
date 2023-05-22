package pl.diminuen.propertysalessystem.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.diminuen.propertysalessystem.security.JwtUtils;
import pl.diminuen.propertysalessystem.security.SecurityUser;
import pl.diminuen.propertysalessystem.services.AuthService;
import pl.diminuen.propertysalessystem.utils.CookieUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${app.oauth2.redirect}")
    private String redirectUri;
    private final CookieUtils cookieUtils;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        super.clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = redirectUri.isEmpty() ?
                determineTargetUrl(request, response, authentication) : redirectUri;

        String token = jwtUtils.generateToken((SecurityUser)authentication.getPrincipal());
        String refreshToken = jwtUtils.generateRefreshToken((SecurityUser)authentication.getPrincipal());

        response.addCookie(cookieUtils.buildAuthTokenCookie(token));
        response.addCookie(cookieUtils.buildRefreshTokenCookie(refreshToken));

        response.sendRedirect(targetUrl);
        //getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
