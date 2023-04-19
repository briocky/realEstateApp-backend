package pl.diminuen.propertysalessystem.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.diminuen.propertysalessystem.dto.UserDto;
import pl.diminuen.propertysalessystem.models.ERole;
import pl.diminuen.propertysalessystem.models.User;
import pl.diminuen.propertysalessystem.repositories.UserRepository;
import pl.diminuen.propertysalessystem.security.SecurityUser;
import pl.diminuen.propertysalessystem.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping(value = "/info")
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal SecurityUser securityUser) {
        UserDto userDto = userService.getUserInfo(securityUser);
        return ResponseEntity.ok(userDto);
    }

}
