package com.example.spotdifference.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    private Integer currentGameNumber = 1;  
    private Integer currentRoundScore = 0;
    private Integer totalScore = 0;
    private Integer currentLevel = 1;
    private Integer currentGameProgress = 0;
    private LocalDateTime createdAt;
    
    public User() {}
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getCurrentGameNumber() { return currentGameNumber; }
    public void setCurrentGameNumber(Integer currentGameNumber) { this.currentGameNumber = currentGameNumber; }
    public Integer getCurrentRoundScore() { return currentRoundScore; }
    public void setCurrentRoundScore(Integer currentRoundScore) { this.currentRoundScore = currentRoundScore; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public Integer getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Integer currentLevel) { this.currentLevel = currentLevel; }
    public Integer getCurrentGameProgress() { return currentGameProgress; }
    public void setCurrentGameProgress(Integer currentGameProgress) { this.currentGameProgress = currentGameProgress; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}