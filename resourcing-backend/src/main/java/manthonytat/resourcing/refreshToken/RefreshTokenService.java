package manthonytat.resourcing.refreshToken;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import manthonytat.resourcing.jwt.JwtService;
import manthonytat.resourcing.user.User;

@Service
public class RefreshTokenService {

  @Value("${app.jwt.refresh-expiration-ms}")
  private Long refreshTokenDurationMs;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  public RefreshToken createRefreshToken(User user) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setToken(this.jwtService.generateRefreshToken(user));
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

    return this.refreshTokenRepository.save(refreshToken);
  }

  public Optional<RefreshToken> findByToken(RefreshToken token) {
    return this.refreshTokenRepository.findByToken(token);
  }

  public boolean isValid(RefreshToken token) {
    return token.getExpiryDate().isAfter(Instant.now());
  }

  public void deleteByUserId(Long userId) {
    this.refreshTokenRepository.deleteById(userId);
  }

  public void delete(RefreshToken token) {
    Optional<RefreshToken> found = this.findByToken(token);
    if (found.isPresent()) {
      this.refreshTokenRepository.delete(found.get());
    }
    throw new IllegalArgumentException("Invalid refresh token");
  }
}