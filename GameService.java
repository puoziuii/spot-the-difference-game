package com.example.spotdifference.service;

import com.example.spotdifference.model.Difference;
import com.example.spotdifference.model.DifferenceGame;
import com.example.spotdifference.model.User;
import com.example.spotdifference.repository.DifferenceRepository;
import com.example.spotdifference.repository.GameRepository;
import com.example.spotdifference.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DifferenceRepository differenceRepository;
    
    // جلب لعبة حسب المستوى ورقم اللعبة
    public DifferenceGame getGameByLevelAndNumber(Integer level, Integer gameNumber) {
        if (level == null || gameNumber == null) {
            System.out.println("❌ getGameByLevelAndNumber: level or gameNumber is null");
            return null;
        }
        System.out.println("🔍 Searching for game: level=" + level + ", gameNumber=" + gameNumber);
        DifferenceGame game = gameRepository.findByLevelAndGameNumber(level, gameNumber);
        if (game == null) {
            System.out.println("❌ No game found for level=" + level + ", gameNumber=" + gameNumber);
        } else {
            System.out.println("✅ Game found: " + game.getImage1Url());
        }
        return game;
    }
    
    // تحديث تقدم اللاعب
    public void updateProgress(User user, int foundCount) {
        user.setCurrentGameProgress(foundCount);
        userRepository.update(user);
    }
    
    // إضافة نقاط الجولة
    public void addRoundPoints(User user, int points) {
        user.setCurrentRoundScore(user.getCurrentRoundScore() + points);
        userRepository.update(user);
    }
    
    // حساب النقاط
    public int calculatePoints(int level, int remainingTime, int totalTime) {
        int basePoints = level == 1 ? 100 : (level == 2 ? 150 : 200);
        double timeBonus = (double) remainingTime / totalTime;
        return (int) (basePoints * (0.5 + timeBonus * 0.5));
    }
    
    // التحقق من الاختلاف (للاستخدام المباشر مع الـ Set)
    public boolean checkDifference(Long gameId, int x, int y, int tolerance) {
        Difference difference = differenceRepository.findByGameAndCoordinates(gameId, x, y, tolerance);
        return difference != null;
    }
}