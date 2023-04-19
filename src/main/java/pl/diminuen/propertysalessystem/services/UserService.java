package pl.diminuen.propertysalessystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.diminuen.propertysalessystem.dto.UserDto;
import pl.diminuen.propertysalessystem.models.User;
import pl.diminuen.propertysalessystem.repositories.UserRepository;
import pl.diminuen.propertysalessystem.security.SecurityUser;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUserInfo(SecurityUser securityUser) {
        User user = userRepository.findByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "No user can be found with email: " + securityUser.getUsername()));
        UserDto userDto = new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPictureUrl()
        );

        return userDto;
    }
}
