package com.ecommerce.shopping.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

  private static final String BEARER_TOKEN_HEADER = "Bearer ";

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    // Authenticate
    if (!StringUtils.isEmpty(authHeader) && authHeader.startsWith(BEARER_TOKEN_HEADER)) {
      String token = authHeader.substring(BEARER_TOKEN_HEADER.length());
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(token, token);

      Authentication auth =
          this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
      SecurityContextHolder.getContext().setAuthentication(auth);

      chain.doFilter(request, response);
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }
}
