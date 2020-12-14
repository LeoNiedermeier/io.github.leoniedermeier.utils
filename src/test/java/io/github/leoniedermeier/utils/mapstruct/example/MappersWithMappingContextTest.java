package io.github.leoniedermeier.utils.mapstruct.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.mapstruct.MappingContext;
import io.github.leoniedermeier.utils.mapstruct.example.DTOs.UniversityDTO;
import io.github.leoniedermeier.utils.mapstruct.example.MappersWithMappingContext.UniversityMapper;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.Department;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.Student;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.University;

class MappersWithMappingContextTest {

    @Test
    void rundtrip() throws Exception {
        Student student = new Student();
        student.setId("myName");

        University university = new University();
        Department department = new Department();
        department.setName("department");
        university.departments.add(department);

        university.students.add(student);
        department.students.add(student);

        UniversityDTO universityDTO = UniversityMapper.INSTANCE.toDTO(university);

        assertEquals("myName", universityDTO.getStudents().get(0).getId());
        assertEquals("myName", universityDTO.getDepartments().get(0).getStudentIds().get(0));

        MappingContext mappingContext = new MappingContext();
        University result = UniversityMapper.INSTANCE.fromDTO(universityDTO, mappingContext);
        mappingContext.resolveReferences();

        assertNotNull(result.getStudents().get(0));
        assertSame(result.getStudents().get(0), result.getDepartments().get(0).getStudents().get(0));

    }
}
