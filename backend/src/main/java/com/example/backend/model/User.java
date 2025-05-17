package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int points = 0;

    @Column(nullable = false)
    private String proficiency;

    @Column(nullable = false)
    private String region;
}