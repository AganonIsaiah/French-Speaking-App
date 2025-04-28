package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @NotNull
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    private String proficiency;

    @Column(nullable = false)
    private String region;

    // SETTERS
    public void setID(Long id) {this.id=id;}
    public void setUsername(String s) {this.username=s;}
    public void setEmail(String s) {this.email=s;}
    public void setPassword(String s) {this.password=s;}
    public void setPoints(int n) {this.points=n;}
    public void setProficiency(String s) {this.proficiency=s;}
    public void setRegion(String s) {this.region=s;}

    // GETTERS
    public Long getID() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public int getPoints() {return points;}
    public String getProficiency() {return proficiency;}
    public String getRegion() {return region;}
}
