package io.github.leoniedermeier.utils.mapstruct.example;

import org.mapstruct.AfterMapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import io.github.leoniedermeier.utils.mapstruct.Identifiable;
import io.github.leoniedermeier.utils.mapstruct.ThreadLocalReferenceResolverHolder;
import io.github.leoniedermeier.utils.mapstruct.example.DTOs.DepartmentDTO;
import io.github.leoniedermeier.utils.mapstruct.example.DTOs.StudentDTO;
import io.github.leoniedermeier.utils.mapstruct.example.DTOs.UniversityDTO;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.Department;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.Student;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.University;

public interface MappersWithThreadLocal {

    @Mapper()
    public interface StudentMapper {

        StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

        StudentDTO toDTO(Student student);

        Student fromDTO(StudentDTO studentDTO);

        @AfterMapping
        default void registerInstance(StudentDTO dto, @MappingTarget Student modell) {
            if (dto != null && modell != null) {
                ThreadLocalReferenceResolverHolder.getReferenceResolver().registerInstance(dto.getId(), modell);
            }
        }
    }

    @Mapper(uses = { StudentMapper.class, DepartmentMapper.class })
    public interface UniversityMapper {

        UniversityMapper INSTANCE = Mappers.getMapper(UniversityMapper.class);

        UniversityDTO toDTO(University university);

        University fromDTO(UniversityDTO universityDTO);
    }

    @Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, uses = StudentMapper.class)
    public interface DepartmentMapper {

        DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

        DepartmentDTO toDTO(Department department);

        @Mapping(target = "students", ignore = true)
        Department fromDTO(DepartmentDTO departmentDTO);

        @AfterMapping
        default void afterFromDTO(DepartmentDTO departmentDTO, @MappingTarget Department department) {
            departmentDTO.getStudentIds().forEach(s -> ThreadLocalReferenceResolverHolder.getReferenceResolver()
                    .registerRefereceTarget(s, department::addStudent));
        }

        @AfterMapping
        default void afterToDTO(Department department, @MappingTarget DepartmentDTO departmentDTO) {
            department.getStudents().stream().map(Identifiable::getId).forEach(departmentDTO::addStudentId);
        }

    }
}
