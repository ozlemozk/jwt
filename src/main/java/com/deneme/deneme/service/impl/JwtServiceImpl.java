package com.deneme.deneme.service.impl;

import com.deneme.deneme.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256).compact();

    }

    private <T> T exteracClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = exteracAllClaim(token);
        return claimsResolvers.apply(claims);
    }
    @Override
    public String extractUserName(String token) {
        return exteracClaim(token, Claims::getSubject);

        //  https://www.youtube.com/watch?v=jQrExUrNbQE&ab_channel=CodeWithProjects   20.41
    }


    private Key getSiginKey() {
        byte[] key = Decoders.BASE64.decode("b3psZW1zaWZyZQo=");
        return Keys.hmacShaKeyFor(key);
    }

    private Claims exteracAllClaim(String token) {
        return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return exteracClaim(token, Claims::getExpiration).before(new Date());
    }

}
