package com.ngondrotracker.server.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ngondrotracker.server.user.model.UserTokenDto;
import com.ngondrotracker.server.user.service.interfaces.UserTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserTokenServiceImpl implements UserTokenService {
    @Value("${user.service.token.lifespan}")
    private Long lifespan;

    @Value("${user.service.token.secret}")
    private String secret;

    @Value("${user.service.token.issuer}")
    private String issuer;

    @Override
    public UserTokenDto generateToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        long expirationDate = System.currentTimeMillis() + lifespan;

        String token = JWT.create()
                .withIssuer(issuer)
                .withClaim("username", username)
                .withClaim("expirationDate", expirationDate)
                .sign(algorithm);

        UserTokenDto tokenDto = new UserTokenDto();
        tokenDto.setToken(token);
        tokenDto.setExpirationDate(expirationDate);

        return tokenDto;
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
