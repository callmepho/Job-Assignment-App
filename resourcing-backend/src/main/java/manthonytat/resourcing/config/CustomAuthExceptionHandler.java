package manthonytat.resourcing.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthExceptionHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    System.out.println(authException.getClass() + "  STATUS OF EXCEPTION");
    response.setStatus(this.chooseStatusCode(authException));
    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write("{ \"message\": \"" + authException.getMessage() + "\"}");
  }

  private int chooseStatusCode(AuthenticationException authException) {
    if (authException instanceof BadCredentialsException) {
      return HttpServletResponse.SC_BAD_REQUEST;
    }

    if (authException instanceof InsufficientAuthenticationException) {
      return HttpServletResponse.SC_UNAUTHORIZED;
    }

    return HttpServletResponse.SC_FORBIDDEN;

  }
}
