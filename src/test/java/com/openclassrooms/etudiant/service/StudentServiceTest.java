package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.dto.StudentDTO;
import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.enums.AccountStatus;
import com.openclassrooms.etudiant.mapper.StudentDtoMapper;
import com.openclassrooms.etudiant.repository.StudentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentDtoMapper studentDtoMapper;

    @InjectMocks
    private StudentService studentService;
    private static final Long STUDENT_ID = 1L;
    private static final String CARD_NUMBER = "CARD-001";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2000, 5, 15);
    private static final String EMAIL = "john.doe@example.com";
    private static final String PHONE_NUMBER = "0123456789";
    private static final String HOME_ADDRESS = "10 Main Street, 35000 Nice Home, Somewhere";
    private static final String MEMBERSHIP_TYPE = "Standard";
    private static final LocalDate MEMBERSHIP_EXPIRY_DATE = LocalDate.of(2027, 12, 31);
    private static final AccountStatus ACCOUNT_STATUS = AccountStatus.ACTIVE;

    @Test
    void createStudentWithValidDataPersistsStudent() {
        // GIVEN
        StudentDTO studentDTO = createStudentDTO(
                null,
                CARD_NUMBER,
                FIRST_NAME,
                LAST_NAME,
                DATE_OF_BIRTH,
                EMAIL,
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );

        Student student = createStudent(
                null,
                CARD_NUMBER,
                FIRST_NAME,
                LAST_NAME,
                DATE_OF_BIRTH,
                EMAIL,
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );

        Student savedStudent = createStudent(
                STUDENT_ID,
                CARD_NUMBER,
                FIRST_NAME,
                LAST_NAME,
                DATE_OF_BIRTH,
                EMAIL,
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );
        StudentDTO expectedDTO = createStudentDTO(
                STUDENT_ID,
                CARD_NUMBER,
                FIRST_NAME,
                LAST_NAME,
                DATE_OF_BIRTH,
                EMAIL,
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );

        when(studentRepository.findByCardNumber(CARD_NUMBER))
                .thenReturn(Optional.empty());
        when(studentDtoMapper.toEntity(studentDTO))
                .thenReturn(student);
        when(studentRepository.save(student))
                .thenReturn(savedStudent);
        when(studentDtoMapper.toDto(savedStudent))
                .thenReturn(expectedDTO);

        // WHEN
        StudentDTO actualDTO = studentService.createStudent(studentDTO);

        // THEN
        assertThat(actualDTO).isEqualTo(expectedDTO);

        verify(studentRepository).findByCardNumber(CARD_NUMBER);
        verify(studentDtoMapper).toEntity(studentDTO);
        
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentCaptor.capture());

        Student capturedStudent = studentCaptor.getValue();

        assertThat(capturedStudent.getCardNumber()).isEqualTo(CARD_NUMBER);
        assertThat(capturedStudent.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(capturedStudent.getLastName()).isEqualTo(LAST_NAME);
        assertThat(capturedStudent.getId()).isNull();        
        verify(studentDtoMapper).toDto(savedStudent);
    }

    @Test
    void getStudentByIdReturnsStudentDTO() {
        // GIVEN
        Student student = createStudent(
                STUDENT_ID,
                CARD_NUMBER,
                FIRST_NAME,
                LAST_NAME,
                DATE_OF_BIRTH,
                EMAIL,
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );

        StudentDTO expectedDTO = createStudentDTO(
                STUDENT_ID,
                CARD_NUMBER,
                FIRST_NAME,
                LAST_NAME,
                DATE_OF_BIRTH,
                EMAIL,
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );

        when(studentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(student));

        when(studentDtoMapper.toDto(student))
                .thenReturn(expectedDTO);
        // WHEN
        StudentDTO actualDTO = studentService.getStudentById(STUDENT_ID);

        // THEN
        assertThat(actualDTO).isEqualTo(expectedDTO);

        verify(studentRepository).findById(STUDENT_ID);
        verify(studentDtoMapper).toDto(student);
    }

    @Test
    void updateStudentWithValidDataUpdatesExistingStudent() {
        // GIVEN
        Student existingStudent = createStudent(
                STUDENT_ID,
                CARD_NUMBER,
                FIRST_NAME,
                LAST_NAME,
                DATE_OF_BIRTH,
                EMAIL,
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );
        StudentDTO updatedStudentDTO = createStudentDTO(
                STUDENT_ID,
                CARD_NUMBER,
                "Jane",
                "Smith",
                DATE_OF_BIRTH,
                "jane.smith@example.com",
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );

        StudentDTO expectedDTO = createStudentDTO(
                STUDENT_ID,
                CARD_NUMBER,
                "Jane",
                "Smith",
                DATE_OF_BIRTH,
                "jane.smith@example.com",
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );

        when(studentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(existingStudent));

        // WHEN
        // Simulate the mapper updates existingStudent in place.
        doAnswer(invocation -> {
            StudentDTO sourceDTO = invocation.getArgument(0);
            Student targetStudent = invocation.getArgument(1);

            targetStudent.setFirstName(sourceDTO.getFirstName());
            targetStudent.setLastName(sourceDTO.getLastName());
            targetStudent.setEmail(sourceDTO.getEmail());

            return null;
        }).when(studentDtoMapper).updateEntityFromDto(updatedStudentDTO, existingStudent);

        when(studentRepository.save(existingStudent))
                .thenReturn(existingStudent);

        when(studentDtoMapper.toDto(existingStudent))
                .thenReturn(expectedDTO);
        // WHEN
        StudentDTO actualDTO = studentService.updateStudent(STUDENT_ID, updatedStudentDTO);

        // THEN
        assertThat(actualDTO).isEqualTo(expectedDTO);

        assertThat(existingStudent.getFirstName()).isEqualTo("Jane");
        assertThat(existingStudent.getLastName()).isEqualTo("Smith");
        assertThat(existingStudent.getEmail()).isEqualTo("jane.smith@example.com");

        verify(studentRepository).findById(STUDENT_ID);
        verify(studentDtoMapper).updateEntityFromDto(updatedStudentDTO, existingStudent);
        verify(studentRepository).save(existingStudent);
        verify(studentDtoMapper).toDto(existingStudent);
    }

    @Test
    void deleteStudentRemovesExistingStudent() {
        // GIVEN
        Student student = createStudent(
                STUDENT_ID,
                CARD_NUMBER,
                FIRST_NAME,
                LAST_NAME,
                DATE_OF_BIRTH,
                EMAIL,
                PHONE_NUMBER,
                HOME_ADDRESS,
                MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE,
                ACCOUNT_STATUS
        );

        when(studentRepository.findById(STUDENT_ID))
                .thenReturn(Optional.of(student));

        // WHEN
        studentService.deleteStudent(STUDENT_ID);

        // THEN
        verify(studentRepository).findById(STUDENT_ID);
        verify(studentRepository).delete(student);
    }

    @Test
    void getAllStudentsReturnsStudentDTOList() {
        // GIVEN
        Student firstStudent = createStudent(
                1L, CARD_NUMBER, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH,
                EMAIL, PHONE_NUMBER, HOME_ADDRESS, MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE, ACCOUNT_STATUS
        );

        Student secondStudent = createStudent(
                2L, "CARD-002", "Jane", "Smith", DATE_OF_BIRTH,
                "jane.smith@example.com", PHONE_NUMBER, HOME_ADDRESS,
                MEMBERSHIP_TYPE, MEMBERSHIP_EXPIRY_DATE, ACCOUNT_STATUS
        );

        StudentDTO firstStudentDTO = createStudentDTO(
                1L, CARD_NUMBER, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH,
                EMAIL, PHONE_NUMBER, HOME_ADDRESS, MEMBERSHIP_TYPE,
                MEMBERSHIP_EXPIRY_DATE, ACCOUNT_STATUS
        );

        StudentDTO secondStudentDTO = createStudentDTO(
                2L, "CARD-002", "Jane", "Smith", DATE_OF_BIRTH,
                "jane.smith@example.com", PHONE_NUMBER, HOME_ADDRESS,
                MEMBERSHIP_TYPE, MEMBERSHIP_EXPIRY_DATE, ACCOUNT_STATUS
        );

        when(studentRepository.findAll())
                .thenReturn(List.of(firstStudent, secondStudent));

        when(studentDtoMapper.toDto(firstStudent))
                .thenReturn(firstStudentDTO);

        when(studentDtoMapper.toDto(secondStudent))
                .thenReturn(secondStudentDTO);

        // WHEN
        List<StudentDTO> actualDTOs = studentService.getAllStudents();

        // THEN
        assertThat(actualDTOs)
                .hasSize(2)
                .containsExactly(firstStudentDTO, secondStudentDTO);

        verify(studentRepository).findAll();
        verify(studentDtoMapper).toDto(firstStudent);
        verify(studentDtoMapper).toDto(secondStudent);
    }

    /**
     * Creates a Student object with the provided parameters.
     * @param id
     * @param cardNumber
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param email
     * @param phoneNumber
     * @param homeAddress
     * @param membershipType
     * @param membershipExpiryDate
     * @param accountStatus
     * @return a student entity object
     */
    private Student createStudent(
            Long id,
            String cardNumber,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email,
            String phoneNumber,
            String homeAddress,
            String membershipType,
            LocalDate membershipExpiryDate,
            AccountStatus accountStatus) {

        Student student = new Student();
        student.setId(id);
        student.setCardNumber(cardNumber);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setDateOfBirth(dateOfBirth);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setHomeAddress(homeAddress);
        student.setMembershipType(membershipType);
        student.setMembershipExpiryDate(membershipExpiryDate);
        student.setAccountStatus(accountStatus);

        return student;
    }
    /**
     * Creates a StudentDTO object with the provided parameters.
     * @param id
     * @param cardNumber
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param email
     * @param phoneNumber
     * @param homeAddress
     * @param membershipType
     * @param membershipExpiryDate
     * @param accountStatus
     * @return a student DTO object
     */
    private StudentDTO createStudentDTO(
            Long id,
            String cardNumber,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email,
            String phoneNumber,
            String homeAddress,
            String membershipType,
            LocalDate membershipExpiryDate,
            AccountStatus accountStatus) {

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(id);
        studentDTO.setCardNumber(cardNumber);
        studentDTO.setFirstName(firstName);
        studentDTO.setLastName(lastName);
        studentDTO.setDateOfBirth(dateOfBirth);
        studentDTO.setEmail(email);
        studentDTO.setPhoneNumber(phoneNumber);
        studentDTO.setHomeAddress(homeAddress);
        studentDTO.setMembershipType(membershipType);
        studentDTO.setStudentCardNumber(null);
        studentDTO.setStudentCardExpiryDate(null);
        studentDTO.setMembershipExpiryDate(membershipExpiryDate);
        studentDTO.setAccountStatus(accountStatus);

        return studentDTO;
    }
}


