package com.example.demo.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "jwt_id") // UUID thường có độ dài 36 ký tự
    String jwtId;

    @Column(name = "user_id", nullable = false)
    int userId;

    @Column(name = "token", columnDefinition = "TEXT", nullable = false)
    String token;

    @Column(name = "expiry_time", nullable = false)
    Instant expiryTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt = Instant.now();

    @Column(name = "revoked", nullable = false)
    boolean revoked = false;

}
