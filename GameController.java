package com.example.spotdifference.controller;

import com.example.spotdifference.model.Difference;
import com.example.spotdifference.model.DifferenceGame;
import com.example.spotdifference.model.User;
import com.example.spotdifference.repository.DifferenceRepository;
import com.example.spotdifference.repository.UserRepository;
import com.example.spotdifference.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DifferenceRepository differenceRepository;
    
    @GetMapping("/game")
    public String startGame(Model model, HttpSession session) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";
        
        System.out.println("=== START GAME ===");
        System.out.println("User: " + user.getUsername());
        System.out.println("Level: " + user.getCurrentLevel());
        System.out.println("Game Number: " + user.getCurrentGameNumber());
        System.out.println("Progress: " + user.getCurrentGameProgress());
        
        // مسح الجلسة القديمة بالكامل
        session.invalidate();
        
        // إنشاء جلسة جديدة
        session = request.getSession(true);
        
        DifferenceGame currentGame = gameService.getGameByLevelAndNumber(user.getCurrentLevel(), user.getCurrentGameNumber());
        
        if (currentGame == null) {
            System.out.println("❌ No game found, redirecting to reset...");
            // إذا لم يتم العثور على لعبة، قم بإعادة تعيين المستخدم
            resetUserToLevel1(user);
            currentGame = gameService.getGameByLevelAndNumber(1, 1);
            if (currentGame == null) {
                return "redirect:/win";
            }
        }
        
        // تهيئة الجلسة
        session.setAttribute("gameId", currentGame.getId());
        session.setAttribute("startTime", System.currentTimeMillis());
        session.setAttribute("foundDifferences", user.getCurrentGameProgress());
        session.setAttribute("foundDifferencesSet", new HashSet<String>());
        session.setAttribute("currentLevel", currentGame.getLevel());
        session.setAttribute("currentGameNumber", currentGame.getGameNumber());
        
        Map<String, Object> gameState = new HashMap<>();
        gameState.put("gameId", currentGame.getId());
        gameState.put("level", currentGame.getLevel());
        gameState.put("gameNumber", currentGame.getGameNumber());
        gameState.put("timeLimit", currentGame.getTimeLimit());
        gameState.put("totalDifferences", currentGame.getNumberOfDifferences());
        gameState.put("image1Url", currentGame.getImage1Url());
        gameState.put("image2Url", currentGame.getImage2Url());
        
        model.addAttribute("game", gameState);
        model.addAttribute("user", user);
        model.addAttribute("foundDifferences", user.getCurrentGameProgress());
        model.addAttribute("totalDifferences", currentGame.getNumberOfDifferences());
        model.addAttribute("level", user.getCurrentLevel());
        model.addAttribute("gameNumber", user.getCurrentGameNumber());
        model.addAttribute("timeLimit", currentGame.getTimeLimit());
        model.addAttribute("roundScore", user.getCurrentRoundScore());
        
        return "game";
    }
    
    @PostMapping(value = "/api/check-difference", produces = "text/plain")
    @ResponseBody
    public String checkDifference(@RequestParam int x, @RequestParam int y, HttpSession session) {
        User user = getCurrentUser();
        if (user == null) return "error";
        
        Long gameId = (Long) session.getAttribute("gameId");
        if (gameId == null) return "error";
        
        Difference difference = differenceRepository.findByGameAndCoordinates(gameId, x, y, 30);
        
        @SuppressWarnings("unchecked")
        Set<String> foundDifferences = (Set<String>) session.getAttribute("foundDifferencesSet");
        if (foundDifferences == null) {
            foundDifferences = new HashSet<>();
            session.setAttribute("foundDifferencesSet", foundDifferences);
        }
        
        if (difference != null) {
            String diffKey = difference.getX() + "," + difference.getY();
            
            if (foundDifferences.contains(diffKey)) {
                return "already_found";
            }
            
            foundDifferences.add(diffKey);
            session.setAttribute("foundDifferencesSet", foundDifferences);
            
            int found = foundDifferences.size();
            session.setAttribute("foundDifferences", found);
            gameService.updateProgress(user, found);
            
            DifferenceGame game = gameService.getGameByLevelAndNumber(user.getCurrentLevel(), user.getCurrentGameNumber());
            int total = game.getNumberOfDifferences();
            
            if (found >= total) {
                long elapsed = (System.currentTimeMillis() - (long) session.getAttribute("startTime")) / 1000;
                int remaining = Math.max(0, game.getTimeLimit() - (int) elapsed);
                int points = gameService.calculatePoints(user.getCurrentLevel(), remaining, game.getTimeLimit());
                gameService.addRoundPoints(user, points);
                
                boolean hasNextGame = gameService.getGameByLevelAndNumber(user.getCurrentLevel(), user.getCurrentGameNumber() + 1) != null;
                boolean hasNextLevel = false;
                boolean isLastGame = false;
                
                if (!hasNextGame) {
                    hasNextLevel = gameService.getGameByLevelAndNumber(user.getCurrentLevel() + 1, 1) != null;
                    if (!hasNextLevel) {
                        isLastGame = true;
                    }
                }
                
                return "complete:" + found + ":" + points + ":" + hasNextGame + ":" + hasNextLevel + ":" + isLastGame;
            }
            return "success:" + found;
        }
        return "fail";
    }
    
    @PostMapping(value = "/api/next-game", produces = "application/json")
    @ResponseBody
    public Map<String, Object> nextGame(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = getCurrentUser();
            
            if (user == null) {
                response.put("error", "User not found");
                return response;
            }
            
            int currentGameNumber = user.getCurrentGameNumber();
            int currentLevel = user.getCurrentLevel();
            
            System.out.println("=== NEXT GAME ===");
            System.out.println("User: " + user.getUsername());
            System.out.println("Current Level: " + currentLevel);
            System.out.println("Current Game: " + currentGameNumber);
            
            // محاولة العثور على اللعبة التالية
            DifferenceGame nextGame = gameService.getGameByLevelAndNumber(currentLevel, currentGameNumber + 1);
            
            if (nextGame != null) {
                System.out.println("✅ Moving to next game: Level " + currentLevel + ", Game " + (currentGameNumber + 1));
                user.setCurrentGameNumber(currentGameNumber + 1);
                user.setCurrentGameProgress(0);
                userRepository.update(user);
                
                session.invalidate();
                
                response.put("success", true);
                response.put("type", "nextGame");
                response.put("newGameNumber", user.getCurrentGameNumber());
                response.put("level", currentLevel);
                return response;
            }
            
            // لا يوجد لعبة تالية، جرب المستوى التالي
            DifferenceGame nextLevelGame = gameService.getGameByLevelAndNumber(currentLevel + 1, 1);
            
            if (nextLevelGame != null) {
                System.out.println("✅ Moving to next level: " + (currentLevel + 1));
                user.setCurrentLevel(currentLevel + 1);
                user.setCurrentGameNumber(1);
                user.setCurrentGameProgress(0);
                user.setCurrentRoundScore(0);
                userRepository.update(user);
                
                session.invalidate();
                
                response.put("success", true);
                response.put("type", "nextLevel");
                response.put("newLevel", user.getCurrentLevel());
                return response;
            }
            
            System.out.println("🏆 All levels completed!");
            response.put("completed", true);
            return response;
            
        } catch (Exception e) {
            System.err.println("Error in nextGame: " + e.getMessage());
            e.printStackTrace();
            response.put("error", e.getMessage());
            return response;
        }
    }
    
    @PostMapping("/api/reset-game")
    @ResponseBody
    public Map<String, Object> resetGame(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = getCurrentUser();
            
            if (user != null) {
                System.out.println("=== RESET GAME ===");
                System.out.println("Resetting user: " + user.getUsername());
                
                // إعادة تعيين جميع قيم المستخدم
                resetUserToLevel1(user);
                
                // مسح الجلسة بالكامل
                session.invalidate();
                
                response.put("success", true);
                System.out.println("✅ User reset successfully");
            } else {
                response.put("success", false);
                response.put("error", "User not found");
            }
        } catch (Exception e) {
            System.err.println("Error in resetGame: " + e.getMessage());
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
    
    private void resetUserToLevel1(User user) {
        user.setCurrentLevel(1);
        user.setCurrentGameNumber(1);
        user.setCurrentGameProgress(0);
        user.setCurrentRoundScore(0);
        user.setTotalScore(0);
        userRepository.update(user);
        System.out.println("User reset: Level=1, Game=1, Score=0");
    }
    
    @GetMapping("/win")
    public String winScreen(Model model) {
        User user = getCurrentUser();
        if (user == null) return "redirect:/login";
        
        model.addAttribute("username", user.getUsername());
        model.addAttribute("score", user.getTotalScore());
        return "win";
    }
    
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username);
        }
        return null;
    }
    
    // إضافة هذا الحقل للـ request (أضفه في بداية الكلاس)
    @Autowired
    private javax.servlet.http.HttpServletRequest request;
}