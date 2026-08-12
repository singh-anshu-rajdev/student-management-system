package com.anshu.student_management_system.Service.ServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
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

    public String generateToken(UserDetails userDetails) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",userDetails.getUsername());
        claims.put("extraKey","extraValue");
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

//    public Boolean isTokenValid(String token, UserDetails userDetails){
//        return Objects.equals(userDetails.getUsername(), getUserName(token)) && !isTokenExpired(token);
//    }
//
//    public String getUserName(String token){
//        return extractClaim(token, claim -> claim.get("username", String.class));
//    }
//
//    private Boolean isTokenExpired(String token){
//        return extractClaim(token, claims -> claims.getExpiration()).before(new Date());
//    }
//
//    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
//        Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//
//    private Claims extractAllClaims(String token){
//        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
//    }
//
//    public String generateToken(UserDetails userDetails){
//        Map<String,Object> claims = new HashMap<>();
//        claims.put("username",userDetails.getUsername());
//        claims.put("extraKey","extraValue");
//        return buildToken(claims);
//    }
//
//    private String buildToken(Map<String, Object> claims){
//        return Jwts.builder().setClaims(claims)
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .signWith(getSignInKey())
//                .compact();
//    }
//
//    private Key getSignInKey(){
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }


}
