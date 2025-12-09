package com.harsha.assignment.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        user.setId("user123");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("ROLE_USER");

        assertEquals("user123", user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("ROLE_USER", user.getRole());
    }

    @Test
    void testUserAllArgsConstructor() {
        User user = new User("user123", "testuser", "password", "ROLE_USER");

        assertEquals("user123", user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("ROLE_USER", user.getRole());
    }

    @Test
    void testUserNoArgsConstructor() {
        User user = new User();
        
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
    }

    @Test
    void testUserDriverRole() {
        User driver = new User();
        driver.setRole("ROLE_DRIVER");

        assertEquals("ROLE_DRIVER", driver.getRole());
    }

    @Test
    void testUserEqualsAndHashCode() {
        User user1 = new User("user123", "testuser", "password", "ROLE_USER");
        User user2 = new User("user123", "testuser", "password", "ROLE_USER");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
