package pl.diminuen.propertysalessystem.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.diminuen.propertysalessystem.dto.auth.AuthTokens;
import pl.diminuen.propertysalessystem.dto.auth.AuthenticationRequest;
import pl.diminuen.propertysalessystem.dto.auth.AuthenticationResponse;
import pl.diminuen.propertysalessystem.dto.auth.RegistrationRequest;
import pl.diminuen.propertysalessystem.services.AuthService;
import pl.diminuen.propertysalessystem.utils.CookieUtils;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final CookieUtils cookieUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        log.info("Authentication request for email: {} has been received. Processing ...", request.getEmail());
        AuthTokens authTokens = authService.authenticateUser(request);
        response.addCookie(cookieUtils.buildRefreshTokenCookie(authTokens.getRefreshToken()));
        log.info("Authentication successful for email: {}", request.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(authTokens.getToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        log.info("Registration request for email: {} has been received. Processing ...", request.getEmail());
        authService.registerUser(request);
        log.info("Registration successful for email: {}", request.getEmail());
        return ResponseEntity.ok(String.format("Successful registration for email: ", request.getEmail()));
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        final String refreshTokenCookieName = "refreshToken";
        log.info("Refresh token request initiated");
        String refreshToken = authService.getRefreshTokenFromHttpOnlyCookie(
                request, refreshTokenCookieName);
        String newToken = authService.generateNewToken(refreshToken);
        log.info("New token generated");
        return ResponseEntity.ok().body(new AuthenticationResponse(newToken));
    }
}
