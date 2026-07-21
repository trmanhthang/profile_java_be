package com.example.profile.config.jwt;

import com.example.profile.config.security.UserPrincipal;
import com.example.profile.shared.constant.AuthenticationMessageConstant;
import com.example.profile.shared.enums.Roles;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${app.jwtSecret}")
    private String secretKey;

    @Value("${app.jwtAccessTokenExpirationInMs}")
    private long accessTokenExpiration;

    @Value("${app.jwtRefreshTokenExpirationInMs}")
    private long refreshTokenExpiration;


    /**
     * Sinh Access Token với custom claims
     */
    public String generateAccessToken(
            UserPrincipal userPrincipal
    ) {
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("version", userPrincipal.getVersion());
        extraClaims.put("role", userPrincipal.getRole());
        extraClaims.put("uid", userPrincipal.getPublicId());

        return Jwts.builder()
                .claims(extraClaims)
                .id(UUID.randomUUID().toString())
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Sinh Refresh Token
     */
    public String generateRefreshToken(UserPrincipal userPrincipal) {
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("version", userPrincipal.getVersion());
        extraClaims.put("role", userPrincipal.getRole());
        extraClaims.put("uid", userPrincipal.getPublicId());

        return Jwts.builder()
                .claims(extraClaims)
                .id(UUID.randomUUID().toString())
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Validate token
     */
    public boolean isTokenValid(
            String token,
            UserPrincipal userPrincipal
    ) {

        Claims claims = extractAllClaims(token);

        int version = (int) claims.get("version");
        Roles role = Roles.valueOf(claims.get("role").toString());
        String uid = claims.get("uid").toString();
        String sub = claims.getSubject();

        return userPrincipal.getVersion() == version
                && userPrincipal.getRole().equals(role)
                && userPrincipal.getPublicId().equals(uid)
                && userPrincipal.getUsername().equals(sub);
    }

    /**
     * Lấy username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Lấy claim bất kỳ
     */
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Parse token
     */
    private Claims extractAllClaims(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException ex) {
            throw new CredentialsExpiredException(AuthenticationMessageConstant.TOKEN_EXPIRED);

        } catch (MalformedJwtException ex) {
            throw new BadCredentialsException(AuthenticationMessageConstant.TOKEN_MALFORMED);

        } catch (UnsupportedJwtException ex) {
            throw new AuthenticationServiceException(AuthenticationMessageConstant.TOKEN_UNSUPPORTED);

        } catch (SignatureException ex) {
            throw new BadCredentialsException(AuthenticationMessageConstant.TOKEN_SIGNATURE_INVALID);

        } catch (IllegalArgumentException ex) {
            throw new InsufficientAuthenticationException(AuthenticationMessageConstant.TOKEN_EMPTY);

        } catch (JwtException ex) {
            throw new BadCredentialsException(AuthenticationMessageConstant.TOKEN_INVALID);
        }
    }

    /**
     * Secret Key
     */
    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public void validateToken(String token) {
        extractAllClaims(token);
    }

}