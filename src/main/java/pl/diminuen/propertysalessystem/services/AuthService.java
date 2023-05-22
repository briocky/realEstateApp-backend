package pl.diminuen.propertysalessystem.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.diminuen.propertysalessystem.dto.auth.AuthTokens;
import pl.diminuen.propertysalessystem.dto.auth.AuthenticationRequest;
import pl.diminuen.propertysalessystem.dto.auth.RegistrationRequest;
import pl.diminuen.propertysalessystem.exceptions.RefreshTokenException;
import pl.diminuen.propertysalessystem.exceptions.RegistrationException;
import pl.diminuen.propertysalessystem.models.ERole;
import pl.diminuen.propertysalessystem.models.Role;
import pl.diminuen.propertysalessystem.models.User;
import pl.diminuen.propertysalessystem.repositories.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthTokens authenticateUser(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtUtils.generateToken(
                (SecurityUser) authentication.getPrincipal());
        String refreshToken = jwtUtils.generateRefreshToken(
                (SecurityUser) authentication.getPrincipal());

        return AuthTokens.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    public void registerUser(RegistrationRequest request) {
        Optional<Role> defaultUserRoleOptional = roleRepository.findByName(ERole.ROLE_USER);
        final Role defaultUserRole = defaultUserRoleOptional.orElseGet(
                () -> roleRepository.save(new Role(ERole.ROLE_USER)));

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

    public String getRefreshTokenFromHttpOnlyCookie(HttpServletRequest request, String cookieName) {
        final String noRefreshTokenErrorMsg = "No refresh token";
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new RefreshTokenException(noRefreshTokenErrorMsg);
    }

    public String generateNewToken(String refreshToken) {
        String userEmail = jwtUtils.extractEmail(refreshToken);

        if(userEmail != null) {
            var userDetails = (SecurityUser)userDetailsService.loadUserByUsername(userEmail);
            return jwtUtils.generateToken(userDetails);
        }
        throw new RefreshTokenException("Refresh token doesn't contain a correct email");
    }
}
