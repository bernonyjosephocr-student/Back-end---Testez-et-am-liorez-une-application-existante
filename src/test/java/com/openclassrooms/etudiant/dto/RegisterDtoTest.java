package com.openclassrooms.etudiant.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterDtoTest {

    // equals()

    @Test
    void shouldReturnTrueForSameObjectOnEquals() {
        RegisterDTO registerDTO = createRegisterDTO();

        assertEquals(registerDTO, registerDTO);
    }

    @Test
    void shouldReturnTrueForEquivalentObjectsOnEquals() {
        RegisterDTO registerDTO1 = createRegisterDTO();
        RegisterDTO registerDTO2 = createRegisterDTO();

        assertEquals(registerDTO1, registerDTO2);
    }

    @Test
    void shouldReturnFalseForDifferentObjectsOnEquals() {
        RegisterDTO registerDTO1 = createRegisterDTO();
        RegisterDTO registerDTO2 = createRegisterDTO();
        registerDTO2.setFirstName("Jane");

        assertNotEquals(registerDTO1, registerDTO2);
    }

    @Test
    void shouldReturnFalseForNullOnEquals() {
        RegisterDTO registerDTO = createRegisterDTO();

        assertNotEquals(null, registerDTO);
    }

    @Test
    void shouldReturnFalseForDifferentClassOnEquals() {
        RegisterDTO registerDTO = createRegisterDTO();
        Object otherObject = new Object();

        assertNotEquals(registerDTO, otherObject);
    }

    @Test
    void shouldReturnFalseWhenOneFieldIsNullOnEquals() {
        RegisterDTO registerDTO1 = createRegisterDTO();
        RegisterDTO registerDTO2 = createRegisterDTO();
        registerDTO2.setLogin(null);

        assertNotEquals(registerDTO1, registerDTO2);
    }

    @Test
    void shouldReturnTrueWhenCorrespondingFieldsAreNullOnEquals() {
        RegisterDTO registerDTO1 = new RegisterDTO();
        RegisterDTO registerDTO2 = new RegisterDTO();

        assertEquals(registerDTO1, registerDTO2);
    }

    // hashCode()

    @Test
    void shouldBeEqualForEquivalentObjectsOnhashCode() {
        RegisterDTO registerDTO1 = createRegisterDTO();
        RegisterDTO registerDTO2 = createRegisterDTO();

        assertEquals(registerDTO1.hashCode(), registerDTO2.hashCode());
    }

    @Test
    void shouldBeDifferentForObjectsWithDifferentFieldsOnhashCode() {
        RegisterDTO registerDTO1 = createRegisterDTO();
        RegisterDTO registerDTO2 = createRegisterDTO();
        registerDTO2.setPassword("differentPassword");

        assertNotEquals(registerDTO1.hashCode(), registerDTO2.hashCode());
    }

    // toString()

    @Test
    void shouldContainExpectedFieldsOnToString() {
        RegisterDTO registerDTO = createRegisterDTO();

        String result = registerDTO.toString();

        assertTrue(result.contains("firstName=John"));
        assertTrue(result.contains("lastName=Doe"));
        assertTrue(result.contains("login=john.doe"));
        assertTrue(result.contains("password=password123"));
    }

    private RegisterDTO createRegisterDTO() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFirstName("John");
        registerDTO.setLastName("Doe");
        registerDTO.setLogin("john.doe");
        registerDTO.setPassword("password123");

        return registerDTO;
    }
}