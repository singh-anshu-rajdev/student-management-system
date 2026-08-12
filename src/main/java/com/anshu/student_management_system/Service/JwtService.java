package com.anshu.student_management_system.Service;

import com.anshu.student_management_system.DTO.LoginResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

public interface JwtService {

    public String getUserName(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public LoginResponseDTO login(UserDetails userDetails);

    public LoginResponseDTO generateTokenFromRefreshToken(UserDetails userDetails, String refreshToken);
}
