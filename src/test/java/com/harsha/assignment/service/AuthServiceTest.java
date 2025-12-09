package com.harsha.assignment.service;

import com.harsha.assignment.exception.BadRequestException;
import com.harsha.assignment.model.User;
import com.harsha.assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user123");
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole("ROLE_USER");
    }

    @Test
    void testRegister_Success() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = authService.register("testuser", "password", "ROLE_USER");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("ROLE_USER", result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(BadRequestException.class, 
            () -> authService.register("testuser", "password", "ROLE_USER"));
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_WithDriverRole() {
        testUser.setRole("ROLE_DRIVER");
        
        when(userRepository.existsByUsername("driver1")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = authService.register("driver1", "password", "ROLE_DRIVER");

        assertNotNull(result);
        assertEquals("ROLE_DRIVER", result.getRole());
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = authService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, 
            () -> authService.loadUserByUsername("nonexistent"));
    }

    @Test
    void testLoadUserByUsername_WithDriverRole() {
        testUser.setRole("ROLE_DRIVER");
        when(userRepository.findByUsername("testdriver")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = authService.loadUserByUsername("testdriver");

        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_DRIVER")));
    }
}
