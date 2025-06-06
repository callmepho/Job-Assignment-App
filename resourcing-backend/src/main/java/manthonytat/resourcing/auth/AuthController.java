package manthonytat.resourcing.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterDTO data) {
    return new ResponseEntity<>(this.authService.register(data), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDTO data) {
    return new ResponseEntity<>(this.authService.login(data), HttpStatus.OK);
  }

  // @PostMapping("/refresh")
  // public ResponseEntity<AuthResponse> refresh(@RequestParam String
  // refreshToken) {
  // return ResponseEntity.ok(this.authService.refreshToken(refreshToken));
  // }

  // @PostMapping("/logout")
  // public ResponseEntity<String> logout(@RequestParam AuthResponse request) {
  // String refreshToken = request.getRefreshToken();
  // this.authService.logout(refreshToken);
  // return ResponseEntity.ok("Logged out successfully");
  // }
}
