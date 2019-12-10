package com.netcracker.testerritto.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import com.auth0.jwt.JWT;
import com.netcracker.testerritto.dao.UserDAO;
import com.netcracker.testerritto.models.User;
import com.netcracker.testerritto.properties.JwtProperties;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
  private UserDAO userDAO;

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDAO userDAO) {
    super(authenticationManager);
    this.userDAO = userDAO;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader(JwtProperties.HEADER_STRING);
    if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }
    Authentication authentication = getUsernamePasswordAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
    String token = request.getHeader(JwtProperties.HEADER_STRING)
        .replace(JwtProperties.TOKEN_PREFIX,"");

    if (token != null) {

      String userEmail = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
          .build()
          .verify(token)
          .getSubject();
      if (userEmail != null) {
        User user = userDAO.getUserByEmail(userEmail);
        UserPrincipal principal = new UserPrincipal(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userEmail, null, principal.getAuthorities());
        return auth;
      }
      return null;
    }
    return null;
  }
}
