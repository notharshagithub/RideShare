package com.harsha.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsha.assignment.dto.AuthResponse;
import com.harsha.assignment.dto.LoginRequest;
import com.harsha.assignment.dto.RegisterRequest;
import com.harsha.assignment.model.User;
import com.harsha.assignment.service.AuthService;
import com.harsha.assignment.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user123");
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole("ROLE_USER");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");
        registerRequest.setRole("ROLE_USER");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void testRegister_Success() throws Exception {
        when(authService.register(anyString(), anyString(), anyString())).thenReturn(testUser);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("test.jwt.token");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test.jwt.token"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));

        verify(authService).register("testuser", "password123", "ROLE_USER");
    }

    @Test
    void testRegister_WithDriverRole() throws Exception {
        registerRequest.setRole("ROLE_DRIVER");
        testUser.setRole("ROLE_DRIVER");

        when(authService.register(anyString(), anyString(), anyString())).thenReturn(testUser);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("test.jwt.token");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ROLE_DRIVER"));
    }

    @Test
    void testLogin_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        org.springframework.security.core.userdetails.User userDetails = 
            new org.springframework.security.core.userdetails.User(
                "testuser", 
                "password", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtUtil.generateToken("testuser", "ROLE_USER")).thenReturn("test.jwt.token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test.jwt.token"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRegister_InvalidRequest_MissingUsername() throws Exception {
        registerRequest.setUsername(null);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(anyString(), anyString(), anyString());
    }

    @Test
    void testRegister_InvalidRequest_MissingPassword() throws Exception {
        registerRequest.setPassword(null);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(anyString(), anyString(), anyString());
    }
}
