package com.harsha.assignment.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateToken() {
        String username = "testuser";
        String role = "ROLE_USER";
        
        String token = jwtUtil.generateToken(username, role);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String username = "testuser";
        String role = "ROLE_USER";
        
        String token = jwtUtil.generateToken(username, role);
        String extractedUsername = jwtUtil.extractUsername(token);
        
        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractRole() {
        String username = "testuser";
        String role = "ROLE_USER";
        
        String token = jwtUtil.generateToken(username, role);
        String extractedRole = jwtUtil.extractRole(token);
        
        assertEquals(role, extractedRole);
    }

    @Test
    void testValidateToken_ValidToken() {
        String username = "testuser";
        String role = "ROLE_USER";
        
        String token = jwtUtil.generateToken(username, role);
        boolean isValid = jwtUtil.validateToken(token, username);
        
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String username = "testuser";
        String role = "ROLE_USER";
        
        String token = jwtUtil.generateToken(username, role);
        boolean isValid = jwtUtil.validateToken(token, "wronguser");
        
        assertFalse(isValid);
    }

    @Test
    void testGenerateToken_WithDriverRole() {
        String username = "driver1";
        String role = "ROLE_DRIVER";
        
        String token = jwtUtil.generateToken(username, role);
        String extractedRole = jwtUtil.extractRole(token);
        
        assertEquals(role, extractedRole);
    }

    @Test
    void testTokenContainsSubject() {
        String username = "testuser";
        String role = "ROLE_USER";
        
        String token = jwtUtil.generateToken(username, role);
        
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }
}
