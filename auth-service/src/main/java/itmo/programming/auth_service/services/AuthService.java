package itmo.programming.auth_service.services;

import io.jsonwebtoken.JwtException;
import itmo.programming.auth_service.dtos.responses.TokenResponseDTO;
import itmo.programming.auth_service.dtos.responses.UserResponseDTO;
import itmo.programming.auth_service.entities.BlacklistedTokenEntity;
import itmo.programming.auth_service.entities.UserEntity;
import itmo.programming.auth_service.exceptions.LogOutException;
import itmo.programming.auth_service.exceptions.RefreshException;
import itmo.programming.auth_service.exceptions.UserAlreadyExistsException;
import itmo.programming.auth_service.exceptions.UserAuthorizationException;
import itmo.programming.auth_service.exceptions.UserChangeNameOrPasswordException;
import itmo.programming.auth_service.exceptions.UserNotFoundException;
import itmo.programming.auth_service.exceptions.UserRegistrationException;
import itmo.programming.auth_service.models.User;
import itmo.programming.auth_service.repositories.TokenBlackListRepository;
import itmo.programming.auth_service.repositories.UserRepository;
import itmo.programming.auth_service.security.JwtUtils;
import itmo.programming.auth_service.security.PasswordUtils;
import itmo.programming.auth_service.utils.mappers.UserMapper;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final String ACCESS_TYPE = "access";
    private static final String REFRESH_TYPE = "refresh";

    private final UserRepository userRepository;
    private final TokenBlackListRepository tokenBlackListRepository;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository,
                       TokenBlackListRepository tokenBlackListRepository,
                       UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.tokenBlackListRepository = tokenBlackListRepository;
        this.userMapper = userMapper;
    }

    public void register(String email, String password) throws UserRegistrationException {
        try {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new UserAlreadyExistsException("User already exists");
            }

            User user = new User(email, PasswordUtils.hash(password));
            userRepository.save(userMapper.toEntity(user));
        } catch (NoSuchAlgorithmException | DataAccessException exception) {
            throw new UserRegistrationException("Registration failed, please retry", exception);
        }
    }

    public TokenResponseDTO changeEmail(String accessToken, String newEmail, Long userId) {
        try {
            String currentEmail = JwtUtils.getSubject(accessToken);

            if (newEmail != null) {
                String finalCurrentEmail = currentEmail;
                UserEntity user = userRepository.findByEmail(currentEmail)
                        .orElseThrow(() -> new UserNotFoundException("User not found: " + finalCurrentEmail));
                user.setEmail(newEmail);
                currentEmail = newEmail;
            }

            return new TokenResponseDTO(currentEmail,
                    JwtUtils.generateAccessToken(new User(userId, currentEmail, null)),
                    JwtUtils.generateRefreshToken(new User(userId, currentEmail, null))
            );

        } catch (JwtException | IllegalArgumentException | DataAccessException exception) {
            throw new UserChangeNameOrPasswordException("Can't change email");
        }
    }

    public TokenResponseDTO changePassword(String accessToken, String newPassword, Long userId) {
        try {
            String currentEmail = JwtUtils.getSubject(accessToken);

            if (newPassword != null) {
                UserEntity user = userRepository.findByEmail(currentEmail)
                        .orElseThrow(() -> new UserNotFoundException("User not found: " + currentEmail));
                user.setPasswordHash(PasswordUtils.hash(newPassword));
            }

            return new TokenResponseDTO(currentEmail,
                    JwtUtils.generateAccessToken(new User(userId, currentEmail, null)),
                    JwtUtils.generateRefreshToken(new User(userId, currentEmail, null))
            );

        } catch (JwtException | IllegalArgumentException | DataAccessException | NoSuchAlgorithmException exception) {
            throw new UserChangeNameOrPasswordException("Can't change password");
        }
    }

    public TokenResponseDTO auth(String email, String password) throws UserAuthorizationException {
        try {
            UserEntity userEntity = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserAuthorizationException("Invalid email or password"));

            User user = userMapper.toModel(userEntity);

            if (user == null ||!PasswordUtils.verify(user.getPasswordHash(), password)) {
                throw new UserAuthorizationException("Invalid email or password");
            }
            return new TokenResponseDTO(user.getEmail(), JwtUtils.generateAccessToken(user), JwtUtils.generateRefreshToken(user));
        } catch (NoSuchAlgorithmException | DataAccessException exception) {
            throw new UserAuthorizationException("Authorization failed, please retry", exception);
        }
    }

    public TokenResponseDTO refresh(String refreshToken) throws RefreshException {
        try {
            if (!JwtUtils.isRefreshToken(refreshToken) || isBlacklisted(refreshToken, ACCESS_TYPE)) {
                throw new RefreshException("Invalid or expired refresh token");
            }

            blacklistToken(refreshToken,REFRESH_TYPE, JwtUtils.getExpirationTime(refreshToken).getTime());

            String currentEmail = JwtUtils.getSubject(refreshToken);

            UserEntity userEntity = userRepository.findByEmail(currentEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + currentEmail));

            User user = userMapper.toModel(userEntity);

            String newAccess = JwtUtils.generateAccessToken(user);
            String newRefresh = JwtUtils.generateRefreshToken(user);

            return new TokenResponseDTO(null, newAccess, newRefresh);
        } catch (DataAccessException exception) {
            throw new RefreshException("Refresh failed, please retry", exception);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new RefreshException("Invalid or expired refresh token", exception);
        }
    }

    public void logout(String accessToken, String refreshToken) {
        try {
            blacklistToken(accessToken, ACCESS_TYPE, JwtUtils.getExpirationTime(accessToken).getTime());
            blacklistToken(refreshToken, REFRESH_TYPE, JwtUtils.getExpirationTime(refreshToken).getTime());
        } catch (DataAccessException exception) {
            throw new LogOutException("LogOut failed, please retry", exception);
        }
    }

    public UserResponseDTO me(String accessToken) {
        try {
            return new UserResponseDTO(JwtUtils.getSubject(accessToken));
        } catch (JwtException | IllegalArgumentException exception) {
            throw new UserAuthorizationException("Invalid or expired token", exception);
        }
    }

    @Transactional
    public void blacklistToken(String token, String tokenType, long expiresMillis) {
        if (expiresMillis <= System.currentTimeMillis()) {
            return;
        }

        try {
            String tokenHash = PasswordUtils.hash(token);
            Instant expiresAt = Instant.ofEpochMilli(expiresMillis);

            BlacklistedTokenEntity entity =
                    new BlacklistedTokenEntity(tokenHash, tokenType, expiresAt);

            tokenBlackListRepository.save(entity);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Token hashing failed", e);
        }
    }

    public boolean isBlacklisted(String token, String tokenType) {
        try {
            String tokenHash = PasswordUtils.hash(token);
            return tokenBlackListRepository.existsByTokenHashAndTokenTypeAndExpiresAtAfter(
                    tokenHash,
                    tokenType,
                    Instant.now()
            );
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Token hashing failed", e);
        }
    }

    @Transactional
    public void cleanupExpiredTokens() {
        tokenBlackListRepository.deleteByExpiresAtBefore(Instant.now());
    }

}
