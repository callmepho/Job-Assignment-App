package manthonytat.resourcing.auth;

import lombok.Getter;
import lombok.Setter;

public class AuthResponse {
  @Getter
  @Setter
  private String token;

  // @Getter
  // @Setter
  // private String refreshToken;

  public AuthResponse() {
  }

  public AuthResponse(String token) {
    this.token = token;
  }
}
