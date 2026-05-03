package com.example.spotdifference.config;

import com.example.spotdifference.model.Difference;
import com.example.spotdifference.model.DifferenceGame;
import com.example.spotdifference.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer {
    
    @Autowired
    private GameRepository gameRepository;
    
    @PostConstruct
    public void init() {
        // مسح البيانات القديمة أولاً
        if (gameRepository.count() > 0) {
            System.out.println("🗑️ Deleting existing games and differences...");
            gameRepository.deleteAll();
        }
        
        System.out.println("📦 Creating new games with differences...");
        
        // ========== المستوى 1 ==========
        
        // اللعبة 1
        DifferenceGame l1g1 = new DifferenceGame(1, 1, 
            "/images/level1/game1/image1.jpg",
            "/images/level1/game1/image2.jpg", 60);
        
        List<Difference> diffs1 = new ArrayList<>();
        diffs1.add(new Difference(l1g1, 1040,616));
        diffs1.add(new Difference(l1g1, 810, 987));
        diffs1.add(new Difference(l1g1, 202, 1056));
        l1g1.setDifferences(diffs1);
        gameRepository.save(l1g1);
        System.out.println("✅ Saved Level 1, Game 1 with " + diffs1.size() + " differences");
        
        // اللعبة 2 (مؤقتة - عدلي الإحداثيات حسب صورك)
        DifferenceGame l1g2 = new DifferenceGame(1, 2,
            "/images/level1/game2/image1.jpg",
            "/images/level1/game2/image2.jpg", 60);
        
        List<Difference> diffs2 = new ArrayList<>();
        diffs2.add(new Difference(l1g2, 345,605));
        diffs2.add(new Difference(l1g2, 471,486));
        diffs2.add(new Difference(l1g2, 538,349));
        l1g2.setDifferences(diffs2);
        gameRepository.save(l1g2);
        System.out.println("✅ Saved Level 1, Game 2 with " + diffs2.size() + " differences");
        
        // اللعبة 3 (مؤقتة - عدلي الإحداثيات حسب صورك)
        DifferenceGame l1g3 = new DifferenceGame(1, 3,
            "/images/level1/game3/image1.jpg",
            "/images/level1/game3/image2.jpg", 60);
        
        List<Difference> diffs3 = new ArrayList<>();
        diffs3.add(new Difference(l1g3, 544,184));
        diffs3.add(new Difference(l1g3, 396,502));
        diffs3.add(new Difference(l1g3, 483,609));
        l1g3.setDifferences(diffs3);
        gameRepository.save(l1g3);
        System.out.println("✅ Saved Level 1, Game 3 with " + diffs3.size() + " differences");
        
        // ========== المستوى 2 (5 اختلافات) ==========
        
        DifferenceGame l2g1 = new DifferenceGame(2, 1,
            "/images/level2/game1/image1.jpg",
            "/images/level2/game1/image2.jpg", 45);
        
        List<Difference> diffs4 = new ArrayList<>();
        diffs4.add(new Difference(l2g1,415, 566));
        diffs4.add(new Difference(l2g1, 319, 127));
        diffs4.add(new Difference(l2g1, 62, 229));
        diffs4.add(new Difference(l2g1, 154, 424));
        diffs4.add(new Difference(l2g1,212, 557));
        l2g1.setDifferences(diffs4);
        gameRepository.save(l2g1);
        System.out.println("✅ Saved Level 2, Game 1 with " + diffs4.size() + " differences");
        
        DifferenceGame l2g2 = new DifferenceGame(2, 2,
            "/images/level2/game2/image1.jpg",
            "/images/level2/game2/image2.jpg", 45);
        
        List<Difference> diffs5 = new ArrayList<>();
        diffs5.add(new Difference(l2g2, 167,288));
        diffs5.add(new Difference(l2g2, 450,130));
        diffs5.add(new Difference(l2g2, 194,521));
        diffs5.add(new Difference(l2g2, 52,329));
        diffs5.add(new Difference(l2g2, 173,426));
        l2g2.setDifferences(diffs5);
        gameRepository.save(l2g2);
        System.out.println("✅ Saved Level 2, Game 2 with " + diffs5.size() + " differences");
        
        DifferenceGame l2g3 = new DifferenceGame(2, 3,
            "/images/level2/game3/image1.jpg",
            "/images/level2/game3/image2.jpg", 45);
        
        List<Difference> diffs6 = new ArrayList<>();
        diffs6.add(new Difference(l2g3, 80, 220));
        diffs6.add(new Difference(l2g3, 220, 120));
        diffs6.add(new Difference(l2g3, 380, 350));
        diffs6.add(new Difference(l2g3, 520, 80));
        diffs6.add(new Difference(l2g3, 600, 400));
        l2g3.setDifferences(diffs6);
        gameRepository.save(l2g3);
        System.out.println("✅ Saved Level 2, Game 3 with " + diffs6.size() + " differences");
        
        // ========== المستوى 3 (7 اختلافات) ==========
        
        DifferenceGame l3g1 = new DifferenceGame(3, 1,
            "/images/level3/game1/image1.jpg",
            "/images/level3/game1/image2.jpg", 30);
        
        List<Difference> diffs7 = new ArrayList<>();
        diffs7.add(new Difference(l3g1, 379, 74));
        diffs7.add(new Difference(l3g1, 341, 542));
        diffs7.add(new Difference(l3g1, 85, 261));
        diffs7.add(new Difference(l3g1, 60, 463));
        diffs7.add(new Difference(l3g1,21, 198));
        diffs7.add(new Difference(l3g1, 440, 576));
        diffs7.add(new Difference(l3g1, 76, 681));
        l3g1.setDifferences(diffs7);
        gameRepository.save(l3g1);
        System.out.println("✅ Saved Level 3, Game 1 with " + diffs7.size() + " differences");
        
        DifferenceGame l3g2 = new DifferenceGame(3, 2,
            "/images/level3/game2/image1.jpg",
            "/images/level3/game2/image2.jpg", 30);
        
        List<Difference> diffs8 = new ArrayList<>();
        diffs8.add(new Difference(l3g2, 556,104));
        diffs8.add(new Difference(l3g2, 321,138));
        diffs8.add(new Difference(l3g2, 192,405));
        diffs8.add(new Difference(l3g2, 157,566));
        diffs8.add(new Difference(l3g2,498,563));
        diffs8.add(new Difference(l3g2, 503,563));
        diffs8.add(new Difference(l3g2, 80,454));
        l3g2.setDifferences(diffs8);
        gameRepository.save(l3g2);
        System.out.println("✅ Saved Level 3, Game 2 with " + diffs8.size() + " differences");
        
        DifferenceGame l3g3 = new DifferenceGame(3, 3,
            "/images/level3/game3/image1.jpg",
            "/images/level3/game3/image2.jpg", 30);
        
        List<Difference> diffs9 = new ArrayList<>();
        diffs9.add(new Difference(l3g3, 40, 300));
        diffs9.add(new Difference(l3g3, 130, 450));
        diffs9.add(new Difference(l3g3, 270, 180));
        diffs9.add(new Difference(l3g3, 360, 400));
        diffs9.add(new Difference(l3g3, 490, 60));
        diffs9.add(new Difference(l3g3, 540, 350));
        diffs9.add(new Difference(l3g3, 620, 150));
        l3g3.setDifferences(diffs9);
        gameRepository.save(l3g3);
        System.out.println("✅ Saved Level 3, Game 3 with " + diffs9.size() + " differences");
        
        System.out.println("🎉 Total: 9 games added to database!");
    }
}