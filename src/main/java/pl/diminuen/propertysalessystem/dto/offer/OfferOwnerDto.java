package pl.diminuen.propertysalessystem.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.diminuen.propertysalessystem.models.RealEstate;
import pl.diminuen.propertysalessystem.models.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class OfferOwnerDto {
    private String firstName;
    private String lastName;
    private String email;
    private String pictureUrl;
    private String phoneNumber;
    private LocalDateTime joinDate;

    public static OfferOwnerDto build(User user) {
        return new OfferOwnerDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPictureUrl(),
                user.getPhoneNumber(),
                user.getJoinDate()
        );
    }
}

