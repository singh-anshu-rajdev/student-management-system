package com.anshu.student_management_system.Service.ServiceImpl;

import com.anshu.student_management_system.DTO.LoginResponseDTO;
import com.anshu.student_management_system.ExceptionHandler.CustomValidationException;
import com.anshu.student_management_system.ExceptionHandler.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.anshu.student_management_system.Service.JwtService;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private Long jwtExpiration;

    @Value("${security.jwt.refresh-expiration-time}")
    private Long refreshTokenExpiration;

    public String getUserName(String token) {
        return extractClaims(token,claim -> claim.get("username",String.class));
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        return claimResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInkey()).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return Objects.equals(userDetails.getUsername(),getUserName(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public LoginResponseDTO login(UserDetails userDetails) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(generateToken(userDetails));
        loginResponseDTO.setRefreshToken(generateRefreshToken(userDetails));
        return loginResponseDTO;
    }

    private String generateToken(UserDetails userDetails) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",userDetails.getUsername());
        return buildToken(claims);
    }

    private String buildToken(Map<String, Object> claims) {

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInkey()).compact();
    }

    private Key getSignInkey() {
        byte[] signInKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(signInKey);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("tokenType", "REFRESH");
        return buildRefreshToken(claims);
    }

    private String buildRefreshToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSignInkey())
                .compact();
    }

    public LoginResponseDTO generateTokenFromRefreshToken(UserDetails userDetails, String refreshToken) {
        ;
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(generateTokenWithRefreshToken(userDetails, refreshToken));
        loginResponseDTO.setRefreshToken(generateRefreshToken(userDetails));
        return loginResponseDTO;
    }

    private String generateTokenWithRefreshToken(UserDetails userDetails, String refreshToken) {
        String username = getUserName(refreshToken);
        if (!isTokenValid(refreshToken,userDetails)) {
            throw new CustomValidationException(ErrorCode.ERR_AP_2001);
        }
        return generateToken(userDetails);
    }

}
