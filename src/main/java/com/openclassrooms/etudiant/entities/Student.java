package com.openclassrooms.etudiant.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.openclassrooms.etudiant.enums.AccountStatus;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor 
@AllArgsConstructor
@Data 
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_number", nullable = false, unique = true, length = 50)
    private String cardNumber;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "home_address", columnDefinition = "TEXT")
    private String homeAddress;

    @Column(name = "membership_type", length = 20)
    private String membershipType;

    @Column(name = "student_card_number", length = 50)
    private String studentCardNumber;

    @Column(name = "student_card_expiry_date")
    private LocalDate studentCardExpiryDate;

    @Column(name = "membership_expiry_date", nullable = false)
    private LocalDate membershipExpiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        if (membershipType == null) {
            membershipType = "Standard";
        }

    }
}