package com.openclassrooms.etudiant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.openclassrooms.etudiant.enums.AccountStatus;

@Data 
public class StudentDTO {

    private Long id;

    @NotBlank(message = "Card number is required")
    @Size(max = 50)
    private String cardNumber;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Email(message = "Email must be valid")
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String phoneNumber;

    private String homeAddress;

    @Size(max = 20)
    private String membershipType;

    @Size(max = 50)
    private String studentCardNumber;

    private LocalDate studentCardExpiryDate;

    @NotNull(message = "Membership expiry date is required")
    private LocalDate membershipExpiryDate;

    @NotNull (message = "Account status is required")
    private AccountStatus accountStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}