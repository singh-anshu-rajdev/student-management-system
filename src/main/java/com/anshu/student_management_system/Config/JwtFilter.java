package com.anshu.student_management_system.Config;

import com.anshu.student_management_system.Service.JwtService;
import com.anshu.student_management_system.Service.ServiceImpl.UserEntityServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserEntityServiceImpl userEntityService;

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(null==authHeader || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        try{
            String token = authHeader.substring(7);
            String userName = jwtService.getUserName(token);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(null!=userName && null==authentication){
                UserDetails userDetails =this.userEntityService.loadUserByUsername(userName);

                if(jwtService.isTokenValid(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationObject =
                            new UsernamePasswordAuthenticationToken(userDetails,null
                                    ,userDetails.getAuthorities());

                    authenticationObject.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationObject);
                }
            }
            filterChain.doFilter(request,response);
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }
    }
}
