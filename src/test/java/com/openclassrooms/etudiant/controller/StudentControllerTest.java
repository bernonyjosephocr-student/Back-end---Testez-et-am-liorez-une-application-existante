package com.openclassrooms.etudiant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.etudiant.dto.LoginRequestDTO;
import com.openclassrooms.etudiant.dto.RegisterDTO;
import com.openclassrooms.etudiant.dto.StudentDTO;
import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.enums.AccountStatus;
import com.openclassrooms.etudiant.repository.StudentRepository;
import com.openclassrooms.etudiant.repository.UserRepository;
import com.openclassrooms.etudiant.service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class StudentControllerTest {

    private static final String STUDENTS_URL = "/api/students";
    private static final String LOGIN_URL = "/api/login";

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String LOGIN = "student-test-user";
    private static final String PASSWORD = "password";

    private static final String CARD_NUMBER = "CARD-001";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2000, 5, 15);
    private static final LocalDate MEMBERSHIP_EXPIRY_DATE = LocalDate.of(2027, 12, 31);

    @Container
    static GenericContainer<?> mysql =
            new GenericContainer<>("mysql:26.7.0")
                    .withEnv("MYSQL_DATABASE", "test")
                    .withEnv("MYSQL_USER", "test")
                    .withEnv("MYSQL_PASSWORD", "test")
                    .withEnv("MYSQL_ROOT_PASSWORD", "root")
                    .withExposedPorts(3306)
                    .waitingFor(Wait.forListeningPort());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    private String jwtToken;

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                () -> "jdbc:mysql://" + mysql.getHost()
                        + ":" + mysql.getMappedPort(3306) + "/test"
        );

        registry.add("spring.datasource.username", () -> "test");
        registry.add("spring.datasource.password", () -> "test");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    /** We need a authenticated user in order to access the student endpoints(JWT token) */
    @BeforeEach
    void setUp() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFirstName(FIRST_NAME);
        registerDTO.setLastName(LAST_NAME);
        registerDTO.setLogin(LOGIN);
        registerDTO.setPassword(PASSWORD);

        userService.register(registerDTO);

        LoginRequestDTO loginDTO = new LoginRequestDTO();
        loginDTO.setLogin(LOGIN);
        loginDTO.setPassword(PASSWORD);

        String response = mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        jwtToken = response;
    }

    @AfterEach
    void cleanUp() {
        studentRepository.deleteAll();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void getAllStudentsReturnsStudentList() throws Exception {
        // GIVEN
        Student student = new Student();
        student.setCardNumber(CARD_NUMBER);
        student.setFirstName(FIRST_NAME);
        student.setLastName(LAST_NAME);
        student.setDateOfBirth(DATE_OF_BIRTH);
        student.setMembershipExpiryDate(MEMBERSHIP_EXPIRY_DATE);
        student.setAccountStatus(AccountStatus.ACTIVE);

        studentRepository.save(student);

        // WHEN / THEN
        mockMvc.perform(MockMvcRequestBuilders.get(STUDENTS_URL)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cardNumber").value(CARD_NUMBER))
                .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$[0].lastName").value(LAST_NAME));
    }

    @Test
    void getStudentByIdReturnsStudent() throws Exception {
        Student student = createStudent();
        Student savedStudent = studentRepository.save(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/{id}", savedStudent.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardNumber").value(savedStudent.getCardNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(savedStudent.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(savedStudent.getLastName()));
    }

    @Test
    void createStudentReturnsCreatedStudent() throws Exception {
        StudentDTO studentDTO = createStudentDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardNumber").value(studentDTO.getCardNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(studentDTO.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(studentDTO.getLastName()));
    }

    @Test
    void updateStudentReturnsUpdatedStudent() throws Exception {
        Student student = createStudent();
        Student savedStudent = studentRepository.save(student);

        StudentDTO studentDTO = createStudentDTO();
        studentDTO.setCardNumber("CARD-001");
        studentDTO.setFirstName("Updated");
        studentDTO.setLastName("Student");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/students/{id}", savedStudent.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardNumber").value("CARD-001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Student"));
    }

    @Test
    void deleteStudentReturnsNoContent() throws Exception {
        Student student = createStudent();
        Student savedStudent = studentRepository.save(student);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/{id}", savedStudent.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        org.assertj.core.api.Assertions.assertThat(studentRepository.findById(savedStudent.getId())).isEmpty();
    }

    private StudentDTO createStudentDTO() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setCardNumber("CARD-002");
        studentDTO.setFirstName("Jane");
        studentDTO.setLastName("Doe");
        studentDTO.setDateOfBirth(LocalDate.of(2001, 3, 20));
        studentDTO.setEmail("jane.doe@example.com");
        studentDTO.setPhoneNumber("0123456789");
        studentDTO.setHomeAddress("10 Main Street, 35000 Nice Home, Somewhere");
        studentDTO.setMembershipType("Standard");
        studentDTO.setMembershipExpiryDate(LocalDate.of(2027, 12, 31));
        studentDTO.setAccountStatus(AccountStatus.ACTIVE);
        return studentDTO;
    }

    private Student createStudent() {
        Student student = new Student();
        student.setCardNumber("CARD-001");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setDateOfBirth(LocalDate.of(2000, 5, 15));
        student.setMembershipExpiryDate(LocalDate.of(2027, 12, 31));
        student.setAccountStatus(AccountStatus.ACTIVE);
        return student;
    }
}