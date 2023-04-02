package pl.diminuen.propertysalessystem.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import pl.diminuen.propertysalessystem.models.ERole;
import pl.diminuen.propertysalessystem.models.Role;
import pl.diminuen.propertysalessystem.models.User;
import pl.diminuen.propertysalessystem.repositories.RoleRepository;
import pl.diminuen.propertysalessystem.repositories.UserRepository;
import pl.diminuen.propertysalessystem.security.SecurityUser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static pl.diminuen.propertysalessystem.security.oauth2.OAuth2Provider.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        SecurityUser mappedUser = getOAuth2UserDetails(userRequest, oAuth2User);
        return mappedUser;
    }

    private SecurityUser getOAuth2UserDetails(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String providerName = userRequest.getClientRegistration().getRegistrationId();
        checkIfProviderIsSupported(providerName);

        GoogleOAuth2User googleOAuth2User = objectMapper.convertValue(oAuth2User.getAttributes(), GoogleOAuth2User.class);
        Optional<User> optionalUser = userRepository.findByEmail(googleOAuth2User.email());
        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new IllegalStateException("Default user role not found in a database."));
        User user;
        if(optionalUser.isEmpty()) {
            user = User.builder()
                    .email(googleOAuth2User.email())
                    .isVerified(googleOAuth2User.emailVerified())
                    .username(null)
                    .phoneNumber(null)
                    .firstName(googleOAuth2User.givenName())
                    .lastName(googleOAuth2User.familyName())
                    .roles(Collections.singletonList(defaultRole))
                    .joinDate(LocalDateTime.now(ZoneId.systemDefault()))
                    .provider(GOOGLE)
                    .build();
            userRepository.save(user);
        } else {
            log.info("Tu muszę zrobić update usera w DB, na wypadek gdyby zmienił dane u dostawcy OAuth2. TODO");
            user = optionalUser.get();
        }

        SecurityUser securityUser = SecurityUser.build(user);
        return securityUser;
    }

    private void checkIfProviderIsSupported(String providerName) {
        Arrays.stream(values())
                .filter((provider) -> providerName.equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> {
                    log.info("An attempt to authenticate with provider: {} has occured!", providerName);
                    return new InternalAuthenticationServiceException(String.format("The OAuth2 provider %s is not supported yet.", providerName));
                });
    }
}
