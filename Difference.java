package com.example.spotdifference.model;

import javax.persistence.*;

@Entity
@Table(name = "differences")
public class Difference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private DifferenceGame game;
    
    @Column(name = "x_coord", nullable = false)
    private Integer x;
    
    @Column(name = "y_coord", nullable = false)
    private Integer y;
    
    private Integer radius = 15; // tolerance
    
    public Difference() {}
    
    public Difference(DifferenceGame game, int x, int y) {
        this.game = game;
        this.x = x;
        this.y = y;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public DifferenceGame getGame() { return game; }
    public void setGame(DifferenceGame game) { this.game = game; }
    
    public Integer getX() { return x; }
    public void setX(Integer x) { this.x = x; }
    
    public Integer getY() { return y; }
    public void setY(Integer y) { this.y = y; }
    
    public Integer getRadius() { return radius; }
    public void setRadius(Integer radius) { this.radius = radius; }
}