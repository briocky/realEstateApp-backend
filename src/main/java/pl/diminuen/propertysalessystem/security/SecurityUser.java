package pl.diminuen.propertysalessystem.security;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.diminuen.propertysalessystem.models.User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails, OAuth2User {
    private Long id;
    private String email;
    private String name;
    @JsonIgnore
    private String password;
    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

    public static SecurityUser build(User user) {
        SecurityUser securityUser = new SecurityUser();
        securityUser.id = user.getId();
        securityUser.email = user.getEmail();
        securityUser.name = user.getUsername();
        securityUser.password = user.getPassword();
        securityUser.authorities = user.getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        securityUser.attributes = null;
        return securityUser;
    }

    public Long getId(){
        return id;
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; //potencjalnie do zmiany, gdy będziemy chcieli zrobić potwierdzanie email'em
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
