package com.example.spotdifference.security;

import com.example.spotdifference.model.User;
import com.example.spotdifference.repository.UserRepository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println(" Searching for user: " + username);
    	
    	User user = userRepository.findByUsername(username);
        if (user == null) {
        	System.out.println(" User not found: " + username);
        	
            throw new UsernameNotFoundException("User not found: " + username);
        }
        System.out.println(" User found: " + user.getUsername());
        System.out.println(" Stored password (encrypted): " + user.getPassword());
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
            .build();

    }
}