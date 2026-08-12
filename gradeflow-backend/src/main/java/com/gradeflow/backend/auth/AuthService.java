package com.gradeflow.backend.auth;

import com.gradeflow.backend.auth.dto.*;
import com.gradeflow.backend.user.User;
import com.gradeflow.backend.user.UserRepository;
import com.gradeflow.backend.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository verificationTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.gradeflow.backend.security.JwtService jwtService;
    private final com.gradeflow.backend.security.JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;

    @Value("${app.verification.token-expiration-ms}")
    private long verificationTokenExpirationMs;

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .status(UserStatus.PENDING_VERIFICATION)
                .build();
        user = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .user(user)
                .expiresAt(Instant.now().plusMillis(verificationTokenExpirationMs))
                .build();
        verificationTokenRepository.save(verificationToken);

        // TODO: mail servisiyle gönderilecek, şimdilik konsola yazalım ki test edelim
        System.out.println("VERIFICATION TOKEN: " + token);

        return new MessageResponse("Registration successful. Please check your email to verify your account.");
    }

    @Transactional
    public MessageResponse verifyEmail(String token) {
        EmailVerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.isUsed()) {
            throw new RuntimeException("This verification link has already been used");
        }
        if (verificationToken.isExpired()) {
            throw new RuntimeException("This verification link has expired");
        }

        User user = verificationToken.getUser();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        verificationToken.setUsedAt(Instant.now());
        verificationTokenRepository.save(verificationToken);

        return new MessageResponse("Email verified successfully. You can now log in.");
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return issueTokenPair(user);
    }

    private AuthResponse issueTokenPair(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenValue = jwtService.generateRefreshTokenValue();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiresAt(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpirationMs()))
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.of(accessToken, refreshTokenValue);
    }
}