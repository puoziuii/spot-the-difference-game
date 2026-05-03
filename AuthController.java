package com.example.spotdifference.controller;

import com.example.spotdifference.model.User;
import com.example.spotdifference.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                              @RequestParam String password,
                              Model model) {
    	System.out.println(" Registering user: " + username);
        System.out.println(" Raw password: " + password);
    	
        if (userRepository.existsByUsername(username)) {
        	System.out.println(" Username already exists: " + username);
        	
        	model.addAttribute("error", "Username already exists!");
            return "register";
        }
        
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(" Encoded password: " + encodedPassword);
        
        User user = new User(username, passwordEncoder.encode(password));
        userRepository.save(user);
        System.out.println(" User registered successfully: " + username);
        
        return "redirect:/login";
    }
}