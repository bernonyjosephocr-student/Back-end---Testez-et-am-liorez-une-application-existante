package com.openclassrooms.etudiant.entities;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    // Constructor
    @Test
    void shouldCreateUserWithNoArgsConstructor() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getLogin());
        assertNull(user.getPassword());
        assertNull(user.getCreated_at());
        assertNull(user.getUpdated_at());
    }

    @Test
    void shouldInitializeAllFieldsWithAllArgsConstructor() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 2, 10, 0);

        User user = new User(
                1L,
                "John",
                "Doe",
                "johndoe",
                "encodedPassword",
                createdAt,
                updatedAt
        );

        assertEquals(1L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe", user.getLogin());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(createdAt, user.getCreated_at());
        assertEquals(updatedAt, user.getUpdated_at());
    }

    // Getters and setters
    @Test
    void shouldSetAndGetAllFields() {
        User user = new User();

        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 2, 10, 0);

        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setLogin("johndoe");
        user.setPassword("encodedPassword");
        user.setCreated_at(createdAt);
        user.setUpdated_at(updatedAt);

        assertEquals(1L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe", user.getLogin());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(createdAt, user.getCreated_at());
        assertEquals(updatedAt, user.getUpdated_at());
    }

    // equals()
    @Test
    void shouldReturnTrueForSameObjectOnEquals() {
        User user = createUser();

        assertEquals(user, user);
    }

    @Test
    void shouldReturnTrueForEquivalentObjectsOnEquals() {
        User user1 = createUser();
        User user2 = createUser();

        assertEquals(user1, user2);
    }

    @Test
    void shouldReturnFalseForDifferentObjectsOnEquals() {
        User user1 = createUser();

        User user2 = createUser();
        user2.setFirstName("Jane");

        assertNotEquals(user1, user2);
    }

    @Test
    void shouldReturnFalseForNullOnEquals() {
        User user = createUser();

        assertNotEquals(null, user);
    }

    // hashCode()
    @Test
    void shouldBeEqualForEquivalentObjectsOnhashCode() {
        User user1 = createUser();
        User user2 = createUser();

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    // toString()
    @Test
    void shouldContainExpectedFieldsOnToString() {
        User user = createUser();

        String result = user.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("firstName=John"));
        assertTrue(result.contains("lastName=Doe"));
        assertTrue(result.contains("login=johndoe"));
        assertTrue(result.contains("password=encodedPassword"));
    }

    // UserDetails methods
    @Test
    void shouldReturnEmptyCollectionOnGetAuthorities() {
        User user = createUser();

        Collection<? extends GrantedAuthority> authorities =
                user.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldReturnLoginOnGetUsername() {
        User user = createUser();

        assertEquals("johndoe", user.getUsername());
    }

    @Test
    void shouldReturnTrueOnIsAccountNonExpired() {
        User user = createUser();

        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void shouldReturnTrueOnIsAccountNonLocked() {
        User user = createUser();

        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void shouldReturnTrueOnIsCredentialsNonExpired() {
        User user = createUser();

        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnTrueOnIsEnabled() {
        User user = createUser();

        assertTrue(user.isEnabled());
    }

    private User createUser() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 2, 10, 0);

        return new User(
                1L,
                "John",
                "Doe",
                "johndoe",
                "encodedPassword",
                createdAt,
                updatedAt
        );
    }
}