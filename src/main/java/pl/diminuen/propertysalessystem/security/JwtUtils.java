package pl.diminuen.propertysalessystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private String jwtSecret;
    @Value("${app.jwt.expiration.minutes}")
    private int jwtExpirationMinutes;
    private final Algorithm signingAlgorithm;

    public JwtUtils(@Value("${app.jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
        signingAlgorithm = Algorithm.HMAC256(jwtSecret);
    }

    public String generateToken(UserDetails userDetails) {
        long jwtExpirationMilis = (long)jwtExpirationMinutes * 60 * 1000;
        Map<String, Object> claims = new HashMap<>();
        JWTCreator.Builder jwtBuilder = JWT.create();

        jwtBuilder
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMilis))
                .withArrayClaim(
                        "authorities",
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toArray(String[]::new)
                );
        return jwtBuilder.sign(signingAlgorithm);
    }

    public String extractEmail(String token) {
        DecodedJWT jwt = JWT.require(signingAlgorithm)
                .build().verify(token);
        String email = jwt.getSubject();
        return email;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(((SecurityUser)userDetails).getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(new Date());
    }
}
