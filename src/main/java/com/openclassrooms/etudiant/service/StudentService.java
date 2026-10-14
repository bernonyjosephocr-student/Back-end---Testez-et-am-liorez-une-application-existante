package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.dto.StudentDTO;
import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.exception.DuplicateStudentException;
import com.openclassrooms.etudiant.mapper.StudentDtoMapper;
import com.openclassrooms.etudiant.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor 
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentDtoMapper studentDtoMapper;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentDtoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        return studentDtoMapper.toDto(findStudentById(id));
    }

    public StudentDTO createStudent(StudentDTO studentDto) {
        if (studentRepository.findByCardNumber(studentDto.getCardNumber()).isPresent()) {
        throw new DuplicateStudentException(
                "A student with card number " + studentDto.getCardNumber() + " already exists");
        }
        Student savedStudent = studentRepository.save(studentDtoMapper.toEntity(studentDto));
        return studentDtoMapper.toDto(savedStudent);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDto) {
        Student student = findStudentById(id);

        studentDtoMapper.updateEntityFromDto(studentDto, student);

        student = studentRepository.save(student);

        return studentDtoMapper.toDto(student);
    }

    public void deleteStudent(Long id) {
        Student student = findStudentById(id);

        studentRepository.delete(student);
    }

    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found with id: " + id
                ));
    }

}