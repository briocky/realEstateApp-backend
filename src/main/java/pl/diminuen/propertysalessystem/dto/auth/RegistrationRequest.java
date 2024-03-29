package pl.diminuen.propertysalessystem.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@NotNull @NotBlank
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String username;
    @Size(min = 5)
    private String password;
    @Size(min = 5)
    private String confirmPassword;
    private String phoneNumber;
    private String pictureUrl;
}
