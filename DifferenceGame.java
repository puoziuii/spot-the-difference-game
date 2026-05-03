package com.example.spotdifference.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
public class DifferenceGame {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer level;
    
    @Column(name = "game_number", nullable = false)
    private Integer gameNumber;
    
    @Column(name = "image1_url", nullable = false)
    private String image1Url;
    
    @Column(name = "image2_url", nullable = false)
    private String image2Url;
    
    @Column(name = "time_limit")
    private Integer timeLimit;
    
    @Column(name = "number_of_differences")
    private Integer numberOfDifferences;
    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Difference> differences = new ArrayList<>();
    
    public DifferenceGame() {}
    
    public DifferenceGame(Integer level,Integer gameNumber, String image1Url, String image2Url, Integer timeLimit) {
        this.level = level;
        this.gameNumber = gameNumber;
        this.image1Url = image1Url;
        this.image2Url = image2Url;
        this.timeLimit = timeLimit;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public Integer getGameNumber() { return gameNumber; }
    public void setGameNumber(Integer gameNumber) { this.gameNumber = gameNumber; }
    
    public String getImage1Url() { return image1Url; }
    public void setImage1Url(String image1Url) { this.image1Url = image1Url; }
    
    public String getImage2Url() { return image2Url; }
    public void setImage2Url(String image2Url) { this.image2Url = image2Url; }
    
    public Integer getTimeLimit() { return timeLimit; }
    public void setTimeLimit(Integer timeLimit) { this.timeLimit = timeLimit; }
    
    public Integer getNumberOfDifferences() { return numberOfDifferences; }
    public void setNumberOfDifferences(Integer numberOfDifferences) { this.numberOfDifferences = numberOfDifferences; }
    
    public List<Difference> getDifferences() { return differences; }
    public void setDifferences(List<Difference> differences) { 
        this.differences = differences;
        this.numberOfDifferences = differences.size();
    }
}