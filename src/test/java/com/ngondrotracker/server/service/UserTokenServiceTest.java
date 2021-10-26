package com.ngondrotracker.server.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ngondrotracker.server.user.model.UserTokenDto;
import com.ngondrotracker.server.user.service.UserTokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserTokenServiceTest extends AbstractServiceTest {
    @InjectMocks
    private UserTokenServiceImpl userTokenService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(userTokenService, "lifespan", 125L);
        ReflectionTestUtils.setField(userTokenService, "secret", "testSecret");
        ReflectionTestUtils.setField(userTokenService, "issuer", "testIssuer");
    }

    @Test
    @DisplayName("generate token")
    public void generateToken() {
        UserTokenDto tokenDto = userTokenService.generateToken("myUsername");

        DecodedJWT decodedToken = JWT.decode(tokenDto.getToken());
        String username = decodedToken.getClaim("username").asString();
        long expirationDate = decodedToken.getClaim("expirationDate").asLong();

        Assertions.assertEquals("myUsername", username);
        Assertions.assertTrue(System.currentTimeMillis() < expirationDate);
    }

    @Test
    @DisplayName("is valid - true")
    public void validateToken() {
        UserTokenDto tokenDto = userTokenService.generateToken("myUsername");
        userTokenService.isValid(tokenDto.getToken());
    }

    @Test
    @DisplayName("is valid - invalid token")
    public void validateInvalidToken() {
        Assertions.assertFalse(userTokenService.isValid("invalidToken"));
    }

    @Test
    @DisplayName("is valid - expired token token")
    public void validateExpiredToken() {
        ReflectionTestUtils.setField(userTokenService, "lifespan", -125L);
        UserTokenDto tokenDto = userTokenService.generateToken("myUsername");
        Assertions.assertFalse(userTokenService.isValid(tokenDto.getToken()));
    }

    @Test
    @DisplayName("get username from token")
    public void getUsernameFromToken() {
        UserTokenDto tokenDto = userTokenService.generateToken("myUsername");
        Assertions.assertEquals("myUsername", userTokenService.getUsernameFromToken(tokenDto.getToken()));
    }

    @Test
    @DisplayName("refresh token")
    public void refreshToken() {
        UserTokenDto currentToken = userTokenService.generateToken("myUsername");
        UserTokenDto newToken = userTokenService.refreshToken(currentToken.getToken());

        DecodedJWT decodedToken = JWT.decode(newToken.getToken());
        String username = decodedToken.getClaim("username").asString();
        long expirationDate = decodedToken.getClaim("expirationDate").asLong();

        Assertions.assertEquals("myUsername", username);
        Assertions.assertTrue(System.currentTimeMillis() < expirationDate);
    }
}
