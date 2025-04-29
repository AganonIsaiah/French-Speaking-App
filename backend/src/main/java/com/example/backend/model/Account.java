package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @Column(nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String email;

    @Column(nullable = false)
    @NotNull
    private String password;

    @Column(nullable = false)
    private int points = 0;

    @Column(nullable = false)
    @NotNull
    private String proficiency;

    @Column(nullable = false)
    @NotNull
    private String region;

    // SETTERS
    public void setId(Long id) {this.id=id;}
    public void setUsername(String s) {this.username=s;}
    public void setEmail(String s) {this.email=s;}
    public void setPassword(String s) {this.password=s;}
    public void setPoints(int n) {this.points=n;}
    public void setProficiency(String s) {this.proficiency=s;}
    public void setRegion(String s) {this.region=s;}

    // GETTERS
    public Long getId() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public int getPoints() {return points;}
    public String getProficiency() {return proficiency;}
    public String getRegion() {return region;}
}
