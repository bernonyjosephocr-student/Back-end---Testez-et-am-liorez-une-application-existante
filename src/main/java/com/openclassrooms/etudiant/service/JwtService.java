package com.openclassrooms.etudiant.service;


import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value ("${jwt.expiration}")
    private Duration expiration;

    /**
     * Get the signing key for JWT token generation.
     *
     * @return the signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }   

    /**
     * Generate a JWT token for the given user details.
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(expiration);

        return Jwts.builder()        
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(getSigningKey())
                .compact();
    }
    /**
     * Extract the username from a JWT.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Check that the token belongs to the given user and has not expired.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check whether the token has expired.
     */
    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expirationDate.before(new Date());
    }

}
