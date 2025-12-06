package com.harsha.assignment.service;

import com.harsha.assignment.dto.AuthResponse;
import com.harsha.assignment.dto.LoginRequest;
import com.harsha.assignment.dto.RegisterRequest;
import com.harsha.assignment.exception.BadRequestException;
import com.harsha.assignment.exception.NotFoundException;
import com.harsha.assignment.model.User;
import com.harsha.assignment.repository.UserRepository;
import com.harsha.assignment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public AuthResponse register(RegisterRequest request) {
        // Validate role
        if (!request.getRole().equals("ROLE_USER") && !request.getRole().equals("ROLE_DRIVER")) {
            throw new BadRequestException("Role must be either ROLE_USER or ROLE_DRIVER");
        }
        
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        
        userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
    
    public AuthResponse login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}
