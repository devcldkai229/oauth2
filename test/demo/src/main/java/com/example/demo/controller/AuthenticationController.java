package com.example.demo.controller;

import com.example.demo.domain.ApiResponse;
import com.example.demo.domain.request.AuthenticationRequest;
import com.example.demo.domain.request.LogoutTokenRequest;
import com.example.demo.domain.request.RefreshTokenRequest;
import com.example.demo.domain.response.AuthenticationResponse;
import com.example.demo.domain.response.RefreshTokenResponse;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/auth")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request) throws ParseException {
        AuthenticationResponse token = authenticationService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .data(token)
                .message("Login success")
                .timestamp(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                        .format(LocalDateTime.now()))
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refresh(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        RefreshTokenResponse token = authenticationService.refresh(request);
        return ResponseEntity.ok(ApiResponse.<RefreshTokenResponse>builder()
                .status(HttpStatus.OK.value())
                .data(token)
                .message("Login success")
                .timestamp(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                        .format(LocalDateTime.now()))
                .build());
    }

    @PostMapping("/revoke")
    public ResponseEntity<ApiResponse<String>> revoke(@RequestBody LogoutTokenRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(null)
                .message("Logout success")
                .timestamp(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                        .format(LocalDateTime.now()))
                .build());
    }

}
