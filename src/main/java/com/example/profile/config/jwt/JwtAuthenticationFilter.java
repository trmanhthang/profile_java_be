package com.example.profile.config.jwt;

import com.example.profile.config.security.CustomUserDetailsService;
import com.example.profile.config.security.UserPrincipal;
import com.example.profile.shared.constant.AuthenticationMessageConstant;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7).trim();

        if (jwt.isBlank()) {
            throw new JwtException(AuthenticationMessageConstant.TOKEN_MISSING);
        }

        try {
            // Validate token (hết hạn, sai chữ ký, malformed...)
            jwtService.validateToken(jwt);

            // Lấy username
            String username = jwtService.extractUsername(jwt);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserPrincipal userPrincipal =
                        customUserDetailsService.loadUserByUsername(username);

                if (!userPrincipal.isEnabled()) {
                    throw new DisabledException(AuthenticationMessageConstant.USER_DISABLE);
                }

                if (!userPrincipal.isAccountNonLocked()) {
                    throw new LockedException(AuthenticationMessageConstant.USER_LOCKED);
                }

                if (!userPrincipal.isAccountNonExpired()) {
                    throw new DisabledException(AuthenticationMessageConstant.USER_NON_EXPIRED);
                }

                if (!userPrincipal.isCredentialsNonExpired()) {
                    throw new DisabledException("Thông tin đăng nhập đã hết hạn.");
                }

                if (jwtService.isTokenValid(jwt, userPrincipal)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userPrincipal,
                                    null,
                                    userPrincipal.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {

            SecurityContextHolder.clearContext();
            throw ex;
        }
    }
}
