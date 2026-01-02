package itmo.programming.auth_service.controllers;

import itmo.programming.auth_service.dtos.requests.ChangeEmailRequestDTO;
import itmo.programming.auth_service.dtos.requests.ChangePasswordRequestDTO;
import itmo.programming.auth_service.dtos.requests.UserRequestDTO;
import itmo.programming.auth_service.dtos.responses.TokenResponseDTO;
import itmo.programming.auth_service.dtos.responses.UserResponseDTO;
import itmo.programming.auth_service.dtos.responses.MessageResponseDTO;
import itmo.programming.auth_service.services.AuthService;
import itmo.programming.auth_service.utils.CookieMaker;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        authService.register(userRequestDTO.getEmail(), userRequestDTO.getPassword());
        return auth(userRequestDTO);
    }

    @PatchMapping("/email")
    public ResponseEntity<UserResponseDTO> changeEmail(@CookieValue("accessToken") String accessToken,
                                @Valid @RequestBody ChangeEmailRequestDTO changeEmailRequestDTO) {
        TokenResponseDTO token = authService.changeEmail(accessToken, changeEmailRequestDTO.getEmail());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createAccessTokenCookie(token.getAccessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createRefreshTokenCookie(token.getRefreshToken()).toString())
                .body(new UserResponseDTO(token.getEmail()));
    }

    @PatchMapping("/password")
    public ResponseEntity<UserResponseDTO> changePassword(@CookieValue("accessToken") String accessToken,
                                   @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        TokenResponseDTO token = authService.changePassword(accessToken, changePasswordRequestDTO.getPassword());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createAccessTokenCookie(token.getAccessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createRefreshTokenCookie(token.getRefreshToken()).toString())
                .body(new UserResponseDTO(token.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> auth(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        TokenResponseDTO token = authService.auth(userRequestDTO.getEmail(), userRequestDTO.getPassword());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createAccessTokenCookie(token.getAccessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createRefreshTokenCookie(token.getRefreshToken()).toString())
                .body(new UserResponseDTO(token.getEmail()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<MessageResponseDTO> refresh(@CookieValue("refreshToken") String refreshToken) {
        TokenResponseDTO token = authService.refresh(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createAccessTokenCookie(token.getAccessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createRefreshTokenCookie(token.getRefreshToken()).toString())
                .body(new MessageResponseDTO("tokens refreshed"));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDTO> logout(@CookieValue("accessToken") String accessToken,
                           @CookieValue("refreshToken") String refreshToken) {
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createExpiredAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, CookieMaker.createExpiredRefreshTokenCookie().toString())
                .body(new MessageResponseDTO("logout successful"));
    }

    @PostMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@CookieValue("accessToken") String accessToken) {
        return ResponseEntity.ok(
                authService.me(accessToken)
        );
    }

}
