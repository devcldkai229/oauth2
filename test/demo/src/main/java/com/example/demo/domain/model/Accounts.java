package com.example.demo.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name="accounts")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Đảm bảo AUTO_INCREMENT
    int id;

    String username;

    String password;

    String email;

}
