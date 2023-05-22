package pl.diminuen.propertysalessystem.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {
    @NotNull @NotBlank
    private String email;
    @NotNull @NotBlank
    private String password;
}
