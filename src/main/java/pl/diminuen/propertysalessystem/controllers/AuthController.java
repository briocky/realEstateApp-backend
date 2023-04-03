package pl.diminuen.propertysalessystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.diminuen.propertysalessystem.dto.AuthenticationRequest;
import pl.diminuen.propertysalessystem.dto.AuthenticationResponse;
import pl.diminuen.propertysalessystem.dto.RegistrationRequest;
import pl.diminuen.propertysalessystem.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("Authentication request for email: {} has been received. Processing ...", request.getEmail());
        String token = authService.authenticateUser(request);
        log.info("Authentication successful for email: {}", request.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        log.info("Registration request for email: {} has been received. Processing ...", request.getEmail());
        authService.registerUser(request);
        log.info("Registration successful for email: {}", request.getEmail());
        return ResponseEntity.ok(String.format("Successful registration for email: ", request.getEmail()));
    }
}
