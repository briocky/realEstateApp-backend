package pl.diminuen.propertysalessystem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.diminuen.propertysalessystem.dto.AuthenticationRequest;
import pl.diminuen.propertysalessystem.dto.AuthenticationResponse;
import pl.diminuen.propertysalessystem.dto.RegistrationRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        String token = "TOKEN_TESTOWY";

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {

        return ResponseEntity.ok("REGISTeR");
    }
}
