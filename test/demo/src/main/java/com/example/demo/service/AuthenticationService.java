package com.example.demo.service;

import com.example.demo.domain.dto.AccountsDto;
import com.example.demo.domain.model.Accounts;
import com.example.demo.domain.model.RefreshToken;
import com.example.demo.domain.request.AuthenticationRequest;
import com.example.demo.domain.request.LogoutTokenRequest;
import com.example.demo.domain.request.RefreshTokenRequest;
import com.example.demo.domain.response.AuthenticationResponse;
import com.example.demo.domain.response.RefreshTokenResponse;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.JwtUtils;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationService {

    UserRepository userRepository;

    RefreshTokenRepository refreshTokenRepository;

    JwtUtils jwtUtils;

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws ParseException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        Accounts loadedUser = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        if(!passwordEncoder.matches(request.getPassword(), loadedUser.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        AccountsDto userRequest = AccountsDto.builder().username(request.getUsername()).build();

        String jwtAccessToken = jwtUtils.generateAccessToken(userRequest);

        String jwtRefreshToken = jwtUtils.generateRefreshToken(userRequest);

        RefreshToken refreshToken = RefreshToken.builder()
                .jwtId(jwtUtils.retrieveJwtId(jwtRefreshToken))
                .token(jwtRefreshToken)
                .expiryTime(jwtUtils.retrieveExpirationTime(jwtRefreshToken).toInstant())
                .userId(loadedUser.getId())
                .revoked(false).createdAt(Instant.now()).build();

        refreshTokenRepository.save(refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .isAuthenticated(true)
                .build();
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) throws ParseException, JOSEException {
        return jwtUtils.refreshToken(request);
    }

    public void logout(LogoutTokenRequest request) throws ParseException, JOSEException {
        jwtUtils.logout(request);
    }



}
