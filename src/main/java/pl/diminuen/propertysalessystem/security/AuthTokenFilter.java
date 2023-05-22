package pl.diminuen.propertysalessystem.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtTokenPrefix = "Bearer";
        final int jwtTokenStartPosition = jwtTokenPrefix.length() + 1;
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwtToken;
        String userEmail = null;

        if(authHeader == null || !authHeader.startsWith(jwtTokenPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(jwtTokenStartPosition);
        try {
            userEmail = jwtUtils.extractEmail(jwtToken);
        } catch(TokenExpiredException ex) {
            final String errorMessage = "Token expired";
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(errorMessage);
            return;
        }

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = (SecurityUser)userDetailsService.loadUserByUsername(userEmail);

            if(jwtUtils.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
