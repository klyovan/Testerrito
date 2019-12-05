package com.netcracker.testerritto.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.testerritto.models.LoginViewModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    // Grab credentials and map them to login viewmodel
    LoginViewModel credentials = null;
    try {
      credentials = new ObjectMapper().readValue(request.getInputStream(), LoginViewModel.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Create login token
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        credentials.getUserEmail(),
        credentials.getPassword(),
        new ArrayList<>());

    // Authenticate user
    Authentication auth = authenticationManager.authenticate(authenticationToken);

    return auth;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    // Grab principal
    UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

    // Create JWT Token
    String token = JWT.create()
        .withSubject(principal.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
        .sign(HMAC512(JwtProperties.SECRET.getBytes()));

    // Add token in response
    response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
  }
}