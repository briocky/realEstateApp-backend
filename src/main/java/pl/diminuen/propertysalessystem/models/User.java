package pl.diminuen.propertysalessystem.models;

import jakarta.persistence.*;
import lombok.*;
import pl.diminuen.propertysalessystem.security.oauth2.OAuth2Provider;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name="users"
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String pictureUrl;
    private String phoneNumber;
    private boolean isVerified;
    private LocalDateTime joinDate;
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    public User(String firstName, String lastName, String username, String email, String password, String phoneNumber, boolean isVerified, LocalDateTime joinDate, OAuth2Provider provider, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.joinDate = joinDate;
        this.provider = provider;
        this.roles = roles;
        this.password = password;
    }

    public User(String firstName, String lastName, String username, String email, String password, String pictureUrl, String phoneNumber, boolean isVerified, LocalDateTime joinDate, OAuth2Provider provider, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.pictureUrl = pictureUrl;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.joinDate = joinDate;
        this.provider = provider;
        this.roles = roles;
    }
}
