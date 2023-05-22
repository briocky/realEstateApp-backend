package pl.diminuen.propertysalessystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.diminuen.propertysalessystem.dto.user.UserDto;
import pl.diminuen.propertysalessystem.dto.user.UserProfileDataDto;
import pl.diminuen.propertysalessystem.models.User;
import pl.diminuen.propertysalessystem.repositories.UserRepository;
import pl.diminuen.propertysalessystem.security.SecurityUser;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUserInfo(SecurityUser securityUser) {
        User user = getUser(securityUser);

        UserDto userDto = new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPictureUrl()
        );

        return userDto;
    }

    public UserProfileDataDto getProfileData(SecurityUser securityUser) {
        User user = getUser(securityUser);
        return UserProfileDataDto.buildFromUser(user);
    }

    private User getUser(SecurityUser securityUser) {
        User user = userRepository.findByEmail(securityUser.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "No user can be found with email: " + securityUser.getUsername()));
        return user;
    }

    public void updateProfileData(long id, UserProfileDataDto userProfileData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "No user can be found with id: " + id));
        user.setUsername(userProfileData.getUsername());
        user.setFirstName(userProfileData.getFirstName());
        user.setLastName(userProfileData.getLastName());
        user.setPhoneNumber(userProfileData.getPhoneNumber());
        user.setPictureUrl(userProfileData.getPictureUrl());

        userRepository.save(user);
    }
}
