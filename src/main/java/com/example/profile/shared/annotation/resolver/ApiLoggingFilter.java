package com.example.profile.shared.annotation.resolver;

import com.example.profile.config.security.UserPrincipal;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final int MAX_PAYLOAD_LENGTH = 2000;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper(request);

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        try {

            filterChain.doFilter(requestWrapper, responseWrapper);

        } finally {

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                UserPrincipal currentUser =
                        authentication != null
                                && authentication.getPrincipal() instanceof UserPrincipal user
                                ? user
                                : null;

                long executionTime =
                        System.currentTimeMillis() - startTime;

                String requestBody = getPayload(
                        requestWrapper.getContentAsByteArray()
                );

                String queryString = request.getQueryString();
                String fullPath = request.getRequestURI() + (queryString != null ? "?" + queryString : "");

                log.info(
                        "Request -> {} ({}) - {} | body: {}",
                        request.getMethod(),
                        fullPath, // Thay RequestURI bằng đường dẫn đầy đủ có Param
                        currentUser,
                        requestBody
                );

                log.info(
                        "Response -> {} ({}) (status: {}) - time: {} ms",
                        request.getMethod(),
                        request.getRequestURI(),
                        responseWrapper.getStatus(),
                        executionTime
                );
            } catch (Exception e) {

                log.error(
                        "ApiLoggingFilter error: {}",
                        e.getMessage()
                );

            } finally {
                responseWrapper.copyBodyToResponse();
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) {

        String uri = request.getRequestURI();

        return uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/actuator")
                || uri.contains("/upload")
                || uri.contains("/file");
    }

    private String getPayload(byte[] content) {

        if (content.length == 0) {
            return "";
        }

        String payload = new String(
                content,
                StandardCharsets.UTF_8
        );

        if (payload.length() > MAX_PAYLOAD_LENGTH) {

            return payload.substring(0, MAX_PAYLOAD_LENGTH)
                    + "...(truncated)";
        }

        return payload;
    }
}
