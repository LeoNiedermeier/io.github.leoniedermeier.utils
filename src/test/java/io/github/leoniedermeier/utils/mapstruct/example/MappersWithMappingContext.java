package io.github.leoniedermeier.utils.mapstruct.example;

import org.mapstruct.AfterMapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import io.github.leoniedermeier.utils.mapstruct.Identifiable;
import io.github.leoniedermeier.utils.mapstruct.MappingContext;
import io.github.leoniedermeier.utils.mapstruct.RegisterInstanceMapper;
import io.github.leoniedermeier.utils.mapstruct.example.DTOs.DepartmentDTO;
import io.github.leoniedermeier.utils.mapstruct.example.DTOs.StudentDTO;
import io.github.leoniedermeier.utils.mapstruct.example.DTOs.UniversityDTO;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.Department;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.Student;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.University;

import org.mapstruct.Mapping;

public interface MappersWithMappingContext {

    @Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, uses = StudentMapper.class)
    public interface DepartmentMapper {

        DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

        @AfterMapping
        default void afterFromDTO(DepartmentDTO departmentDTO, @MappingTarget Department department,
                @Context MappingContext mappingContext) {
            departmentDTO.getStudentIds()
                    .forEach(s -> mappingContext.registerRefereceTarget(s, department::addStudent));
        }

        @AfterMapping
        default void afterToDTO(Department department, @MappingTarget DepartmentDTO departmentDTO) {
            department.getStudents().stream().map(Identifiable::getId).forEach(departmentDTO::addStudentId);
        }

        @Mapping(target = "students", ignore = true)
        Department fromDTO(DepartmentDTO departmentDTO, @Context MappingContext mappingContext);

        DepartmentDTO toDTO(Department department);

    }

    @Mapper()
    public interface StudentMapper extends RegisterInstanceMapper<StudentDTO, Student> {

        StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

        Student fromDTO(StudentDTO studentDTO, @Context MappingContext mappingContext);

        StudentDTO toDTO(Student student);

    }

    @Mapper(uses = { StudentMapper.class, DepartmentMapper.class })
    public interface UniversityMapper {

        UniversityMapper INSTANCE = Mappers.getMapper(UniversityMapper.class);

        University fromDTO(UniversityDTO universityDTO, @Context MappingContext mappingContext);

        UniversityDTO toDTO(University university);
    }
}
