package com.openclassrooms.etudiant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.openclassrooms.etudiant.dto.StudentDTO;
import com.openclassrooms.etudiant.entities.Student;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR)
public interface StudentDtoMapper {

    StudentDTO toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Student toEntity(StudentDTO studentDto);
    
    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(StudentDTO studentDto, @MappingTarget Student student);
}
