package com.example.demo.domain.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
public class AuthenticationResponse {
    String accessToken;

    String refreshToken;

    boolean isAuthenticated;
}
