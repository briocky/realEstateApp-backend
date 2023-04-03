package pl.diminuen.propertysalessystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.diminuen.propertysalessystem.dto.AuthenticationRequest;
import pl.diminuen.propertysalessystem.dto.RegistrationRequest;
import pl.diminuen.propertysalessystem.exceptions.RegistrationException;
import pl.diminuen.propertysalessystem.models.ERole;
import pl.diminuen.propertysalessystem.models.Role;
import pl.diminuen.propertysalessystem.models.User;
import pl.diminuen.propertysalessystem.repositories.UserRepository;
import pl.diminuen.propertysalessystem.security.JwtUtils;
import pl.diminuen.propertysalessystem.security.SecurityUser;
import pl.diminuen.propertysalessystem.security.oauth2.OAuth2Provider;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public String authenticateUser(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtUtils.generateToken(
                (SecurityUser) authentication.getPrincipal()
        );

        return token;
    }

    public void registerUser(RegistrationRequest request) {
        final Role defaultUserRole = new Role(ERole.ROLE_USER);
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if(optionalUser.isPresent()) {
            throw new RegistrationException("Email taken!");
        } else if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RegistrationException("Passwords aren't equal!");
        }

        var user = new User(
                request.getFirstName(),
                request.getLastName(),
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber(),
                true, //TODO: there is no verification email RN, but if it'll change I have to correct this
                LocalDateTime.now(ZoneId.systemDefault()),
                OAuth2Provider.LOCAL,
                Collections.singletonList(defaultUserRole)
        );

        userRepository.save(user);
    }

}
