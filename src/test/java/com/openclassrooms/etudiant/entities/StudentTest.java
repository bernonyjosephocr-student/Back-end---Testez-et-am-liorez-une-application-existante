package com.openclassrooms.etudiant.entities;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void shouldInitializeAllFieldsWithAllArgsConstructor() {
        LocalDate dateOfBirth = LocalDate.of(2000, 1, 15);
        LocalDate studentCardExpiryDate = LocalDate.of(2027, 12, 31);
        LocalDate membershipExpiryDate = LocalDate.of(2027, 12, 31);
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 2, 10, 0);

        Student student = new Student(
                1L,
                "CARD001",
                "John",
                "Doe",
                dateOfBirth,
                "john.doe@example.com",
                "0600000000",
                "10 Main Street",
                "Premium",
                "STUDENT001",
                studentCardExpiryDate,
                membershipExpiryDate,
                null,
                createdAt,
                updatedAt
        );

        assertEquals(1L, student.getId());
        assertEquals("CARD001", student.getCardNumber());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals(dateOfBirth, student.getDateOfBirth());
        assertEquals("john.doe@example.com", student.getEmail());
        assertEquals("0600000000", student.getPhoneNumber());
        assertEquals("10 Main Street", student.getHomeAddress());
        assertEquals("Premium", student.getMembershipType());
        assertEquals("STUDENT001", student.getStudentCardNumber());
        assertEquals(studentCardExpiryDate, student.getStudentCardExpiryDate());
        assertEquals(membershipExpiryDate, student.getMembershipExpiryDate());
        assertNull(student.getAccountStatus());
        assertEquals(createdAt, student.getCreatedAt());
        assertEquals(updatedAt, student.getUpdatedAt());
    }

    
    // equals()
    @Test
    void shouldReturnTrueForSameObjectOnEquals() {
        Student student = createStudent();

        assertEquals(student, student);
    }

    @Test
    void shouldReturnTrueForEquivalentObjectsOnEquals() {
        Student student1 = createStudent();
        Student student2 = createStudent();

        assertEquals(student1, student2);
    }

    @Test
    void shouldReturnFalseForDifferentObjectsOnEquals() {
        Student student1 = createStudent();

        Student student2 = createStudent();
        student2.setFirstName("Jane");

        assertNotEquals(student1, student2);
    }

    @Test
    void shouldReturnFalseForNullOnEquals() {
        Student student = createStudent();

        assertNotEquals(null, student);
    }

    // hashCode()    
    @Test
    void shouldBeEqualForEquivalentObjectsOnhashCode() {
        Student student1 = createStudent();
        Student student2 = createStudent();

        assertEquals(student1.hashCode(), student2.hashCode());
    }

    
    // toString()
    @Test
    void shouldContainExpectedFieldsOnToString() {
        Student student = createStudent();

        String result = student.toString();
        System.out.println(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("cardNumber=CARD001"));
        assertTrue(result.contains("firstName=John"));
        assertTrue(result.contains("lastName=Doe"));
        assertTrue(result.contains("email=john.doe@example.com"));
        assertTrue(result.contains("membershipType=Premium"));
    }

    // onCreate()
    @Test
    void shouldSetDefaultMembershipTypeWhenNullOnCreate() {
        Student student = createStudent();
        student.setMembershipType(null);

        student.onCreate();

        assertEquals("Standard", student.getMembershipType());
    }

    @Test
    void shouldPreserveExistingMembershipTypeOnCreate() {
        Student student = createStudent();
        student.setMembershipType("Premium");

        student.onCreate();

        assertEquals("Premium", student.getMembershipType());
    }

    private Student createStudent() {
        LocalDate dateOfBirth = LocalDate.of(2000, 1, 15);
        LocalDate studentCardExpiryDate = LocalDate.of(2027, 12, 31);
        LocalDate membershipExpiryDate = LocalDate.of(2027, 12, 31);
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 2, 10, 0);

        return new Student(
                1L,
                "CARD001",
                "John",
                "Doe",
                dateOfBirth,
                "john.doe@example.com",
                "0600000000",
                "10 Main Street",
                "Premium",
                "STUDENT001",
                studentCardExpiryDate,
                membershipExpiryDate,
                null,
                createdAt,
                updatedAt
        );
    }

}