import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ngondrotracker.token.dto.TokenDto;
import com.ngondrotracker.token.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = {"test"})
@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTest {
    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(tokenService, "lifespan", 125L);
        ReflectionTestUtils.setField(tokenService, "secret", "testSecret");
        ReflectionTestUtils.setField(tokenService, "issuer", "testIssuer");
    }

    @Test
    public void generateToken() {
        TokenDto tokenDto = tokenService.generateToken("myUsername");

        DecodedJWT decodedToken = JWT.decode(tokenDto.getToken());
        String username = decodedToken.getClaim("username").asString();
        long expirationDate = decodedToken.getClaim("expirationDate").asLong();

        assertEquals("myUsername", username);
        assertTrue(System.currentTimeMillis() < expirationDate);
    }

    @Test
    public void validateToken() {
        TokenDto tokenDto = tokenService.generateToken("myUsername");
        assertTrue(tokenService.isValid(tokenDto.getToken()));
    }

    @Test
    public void validateInvalidToken() {
        assertFalse(tokenService.isValid("invalidToken"));
    }

    @Test
    public void validateExpiredToken() {
        ReflectionTestUtils.setField(tokenService, "lifespan", -125L);
        TokenDto tokenDto = tokenService.generateToken("myUsername");
        assertFalse(tokenService.isValid(tokenDto.getToken()));
    }

    @Test
    public void getUsernameFromToken() {
        TokenDto tokenDto = tokenService.generateToken("myUsername");
        assertEquals("myUsername", tokenService.getUsernameFromToken(tokenDto.getToken()));
    }

    @Test
    public void refreshToken() {
        TokenDto currentToken = tokenService.generateToken("myUsername");
        TokenDto newToken = tokenService.refreshToken(currentToken.getToken());

        DecodedJWT decodedToken = JWT.decode(newToken.getToken());
        String username = decodedToken.getClaim("username").asString();
        long expirationDate = decodedToken.getClaim("expirationDate").asLong();

        assertEquals("myUsername", username);
        assertTrue(System.currentTimeMillis() < expirationDate);
    }
}
