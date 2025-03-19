package manthonytat.resourcing.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import manthonytat.resourcing.jwt.JwtService;
import manthonytat.resourcing.temp.TempService;
import manthonytat.resourcing.user.User;
import manthonytat.resourcing.user.UserService;

@Service
@Transactional
public class AuthService {
  @Autowired
  private TempService tempService;

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  // @Autowired
  // private RefreshTokenService refreshTokenService;

  @Autowired
  private AuthenticationManager authenticationManager;

  public AuthResponse register(RegisterDTO data) {
    String encodedPass = passwordEncoder.encode(data.getPassword());
    data.setPassword(encodedPass);

    User newUser = this.tempService.create(data);

    String token = this.jwtService.generateToken(newUser);
    return new AuthResponse(token);
  }

  public AuthResponse login(LoginDTO data) {
    UsernamePasswordAuthenticationToken userPassToken = new UsernamePasswordAuthenticationToken(data.getEmail(),
        data.getPassword());

    authenticationManager.authenticate(userPassToken);

    User user = this.userService.getByEmail(data.getEmail());

    if (user == null) {
      return null;
    }

    String token = this.jwtService.generateToken(user);
    // String refreshToken = this.jwtService.generateRefreshToken(user);
    return new AuthResponse(token);
  }

  // public AuthResponse refreshToken(String refreshToken) {
  // Long id = this.jwtService.extractUserId(refreshToken);

  // // check refreshToken db for token
  // Optional<RefreshToken> foundRefreshToken =
  // this.refreshTokenService.findByToken(refreshToken);
  // if (!foundRefreshToken.isPresent() ||
  // !this.refreshTokenService.isValid(foundRefreshToken.get())) {
  // new RuntimeException("Invalid or expired refresh token");
  // }

  // User user = this.userService.getById(id);
  // String newAccessToken = this.jwtService.generateToken(user);

  // return new AuthResponse(newAccessToken, refreshToken);
  // }

}
