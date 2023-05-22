package pl.diminuen.propertysalessystem.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.diminuen.propertysalessystem.dto.user.UserDto;
import pl.diminuen.propertysalessystem.dto.user.UserProfileDataDto;
import pl.diminuen.propertysalessystem.security.SecurityUser;
import pl.diminuen.propertysalessystem.services.UserService;

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

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping(value = "/profile")
    public ResponseEntity<UserProfileDataDto> getUserProfileData(@AuthenticationPrincipal SecurityUser securityUser) {
        return ResponseEntity.ok(userService.getProfileData(securityUser));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PutMapping(value = "/profile/change/{id}")
    public ResponseEntity<?> updateProfileData(@PathVariable long id,
                                               @RequestBody UserProfileDataDto userProfileData) {
        userService.updateProfileData(id, userProfileData);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Profile data updated!");
    }

}
