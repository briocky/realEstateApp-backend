package pl.diminuen.propertysalessystem.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.diminuen.propertysalessystem.models.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserProfileDataDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String pictureUrl;
    private String phoneNumber;
    private boolean isVerified;
    private LocalDateTime joinDate;

    public static UserProfileDataDto buildFromUser(User user) {
        return UserProfileDataDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .joinDate(user.getJoinDate())
                .pictureUrl(user.getPictureUrl())
                .isVerified(user.isVerified())
                .phoneNumber(user.getPhoneNumber())
                .username(user.getUsername())
                .build();
    }
}
