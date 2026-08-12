package com.example.profile.config.permission;

import com.example.profile.config.security.UserPrincipal;
import com.example.profile.modules.permission.service.IUserPermissionService;
import com.example.profile.shared.enums.MethodApi;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiPermissionFilter extends OncePerRequestFilter {

    private final IUserPermissionService userPermissionService;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            filterChain.doFilter(
                    request,
                    response
            );
            return;
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String method = request.getMethod();
        String path = request.getRequestURI();

        boolean hasPermission = this.userPermissionService.hasPermission(
                principal.getId(),
                principal.getRole(),
                MethodApi.valueOf(method.toUpperCase()),
                path
        );

        if (!hasPermission) {
            log.warn("User {} không có quyền {} {}", principal.getUsername(), method, path);

            throw new AccessDeniedException("Bạn không có quyền truy cập API này");
        }

        filterChain.doFilter(request, response);
    }
}
