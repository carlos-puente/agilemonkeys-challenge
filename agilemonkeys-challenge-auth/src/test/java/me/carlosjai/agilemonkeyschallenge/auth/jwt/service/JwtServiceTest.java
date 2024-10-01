package me.carlosjai.agilemonkeyschallenge.auth.jwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

  private JwtService jwtService;

  @Mock
  private UserDetails userDetails;

  private final String secretKey = "YourSecretKeyForHS256EncryptionShouldBeBase64EncodedAndSecure"; // Use your real key
  private final long jwtExpiration = 1000 * 60 * 60; // 1 hour expiration for example

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    jwtService = new JwtService();
    jwtService.secretKey = secretKey;
    jwtService.jwtExpiration = jwtExpiration;
  }

  @Test
  void generateToken_shouldReturnValidToken() {
    when(userDetails.getUsername()).thenReturn("testuser");

    String token = jwtService.generateToken(userDetails);

    assertNotNull(token);
    assertEquals("testuser", jwtService.extractUsername(token));
  }

  @Test
  void isTokenValid_shouldReturnTrueForValidToken() {
    when(userDetails.getUsername()).thenReturn("testuser");
    String token = jwtService.generateToken(userDetails);

    boolean isValid = jwtService.isTokenValid(token, userDetails);

    assertTrue(isValid);
  }

  @Test
  void isTokenValid_shouldReturnFalseForInvalidUsername() {
    when(userDetails.getUsername()).thenReturn("testuser");
    String token = jwtService.generateToken(userDetails);

    when(userDetails.getUsername()).thenReturn("differentuser");

    boolean isValid = jwtService.isTokenValid(token, userDetails);

    assertFalse(isValid);
  }


  private String generateMockToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(Claims.SUBJECT, username);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSignInKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }
}