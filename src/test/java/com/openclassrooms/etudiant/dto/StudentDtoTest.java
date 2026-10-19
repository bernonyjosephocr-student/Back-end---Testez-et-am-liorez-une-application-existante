package com.openclassrooms.etudiant.dto;

import com.openclassrooms.etudiant.enums.AccountStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StudentDtoTest {

    @Test
    void shouldGetAndSetAllFields() {
        StudentDTO student = new StudentDTO();

        LocalDate dateOfBirth = LocalDate.of(2000, 5, 15);
        LocalDate studentCardExpiryDate = LocalDate.of(2027, 12, 31);
        LocalDate membershipExpiryDate = LocalDate.of(2027, 6, 30);
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 10, 10, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 2, 15, 14, 45);

        student.setId(1L);
        student.setCardNumber("CARD-001");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setDateOfBirth(dateOfBirth);
        student.setEmail("john.doe@example.com");
        student.setPhoneNumber("0612345678");
        student.setHomeAddress("10 Main Street");
        student.setMembershipType("Standard");
        student.setStudentCardNumber("STUDENT-001");
        student.setStudentCardExpiryDate(studentCardExpiryDate);
        student.setMembershipExpiryDate(membershipExpiryDate);
        student.setAccountStatus(AccountStatus.ACTIVE);
        student.setCreatedAt(createdAt);
        student.setUpdatedAt(updatedAt);

        assertEquals(1L, student.getId());
        assertEquals("CARD-001", student.getCardNumber());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals(dateOfBirth, student.getDateOfBirth());
        assertEquals("john.doe@example.com", student.getEmail());
        assertEquals("0612345678", student.getPhoneNumber());
        assertEquals("10 Main Street", student.getHomeAddress());
        assertEquals("Standard", student.getMembershipType());
        assertEquals("STUDENT-001", student.getStudentCardNumber());
        assertEquals(studentCardExpiryDate, student.getStudentCardExpiryDate());
        assertEquals(membershipExpiryDate, student.getMembershipExpiryDate());
        assertEquals(AccountStatus.ACTIVE, student.getAccountStatus());
        assertEquals(createdAt, student.getCreatedAt());
        assertEquals(updatedAt, student.getUpdatedAt());
    }

    @Test
    void shouldCreateEqualObjectsWhenFieldsAreIdentical() {
        StudentDTO first = createStudentDTO();
        StudentDTO second = createStudentDTO();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenFieldsDiffer() {
        StudentDTO first = createStudentDTO();
        StudentDTO second = createStudentDTO();

        second.setFirstName("Jane");

        assertNotEquals(first, second);
    }

    @Test
    void shouldGenerateStringRepresentation() {
        StudentDTO student = createStudentDTO();

        String result = student.toString();

        assertNotNull(result);
        assertTrue(result.contains("CARD-001"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }

    @Test
    void shouldHandleNullValues() {
        StudentDTO first = new StudentDTO();
        StudentDTO second = new StudentDTO();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotNull(first.toString());
    }

    private StudentDTO createStudentDTO() {
        StudentDTO student = new StudentDTO();

        student.setId(1L);
        student.setCardNumber("CARD-001");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setDateOfBirth(LocalDate.of(2000, 5, 15));
        student.setEmail("john.doe@example.com");
        student.setPhoneNumber("0612345678");
        student.setHomeAddress("10 Main Street");
        student.setMembershipType("Standard");
        student.setStudentCardNumber("STUDENT-001");
        student.setStudentCardExpiryDate(LocalDate.of(2027, 12, 31));
        student.setMembershipExpiryDate(LocalDate.of(2027, 6, 30));
        student.setAccountStatus(AccountStatus.ACTIVE);
        student.setCreatedAt(LocalDateTime.of(2026, 1, 10, 10, 30));
        student.setUpdatedAt(LocalDateTime.of(2026, 2, 15, 14, 45));

        return student;
    }
}