package com.ngondrotracker.token.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ngondrotracker.token.dto.TokenDto;
import com.ngondrotracker.token.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:token-${spring.profiles.active:}.properties")
public class TokenServiceImpl implements TokenService {
    @Value("${token.lifespan}")
    private Long lifespan;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.issuer}")
    private String issuer;

    @Override
    public TokenDto generateToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        long expirationDate = System.currentTimeMillis() + lifespan;

        String token = JWT.create()
                .withIssuer(issuer)
                .withClaim("username", username)
                .withClaim("expirationDate", expirationDate)
                .sign(algorithm);

        return new TokenDto(token, expirationDate);
    }

    @Override
    public boolean isValid(String token) {
        try {
            decode(token);

            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        return decode(token).getClaim("username").asString();
    }

    @Override
    public TokenDto refreshToken(String currentToken) {
        return generateToken(decode(currentToken).getClaim("username").asString());
    }

    private DecodedJWT decode(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        DecodedJWT decodedToken = verifier.verify(token);
        if (decodedToken.getClaim("expirationDate").asLong() < System.currentTimeMillis()) {
            throw new JWTVerificationException("Token has expired.");
        }

        return decodedToken;
    }
}
