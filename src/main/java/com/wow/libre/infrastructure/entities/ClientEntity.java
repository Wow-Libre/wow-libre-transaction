package com.wow.libre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Data
@Entity
@Table(name = "client")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String username;
    private String jwt;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "expiration_date")
    private Date expirationDate;
}
