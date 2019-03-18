package io.github.leoniedermeier.utils.mapstruct.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.leoniedermeier.utils.mapstruct.ThreadLocalReferenceResolverHolder;
import io.github.leoniedermeier.utils.mapstruct.example.DTOs.UniversityDTO;
import io.github.leoniedermeier.utils.mapstruct.example.MappersWithThreadLocal.UniversityMapper;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.Department;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.Student;
import io.github.leoniedermeier.utils.mapstruct.example.Modell.University;

public class MappersWithTreadLocalTest {

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

        final University result;
        try {
            result = UniversityMapper.INSTANCE.fromDTO(universityDTO);
            ThreadLocalReferenceResolverHolder.getReferenceResolver().resolveReferences();
            
        }finally {
            ThreadLocalReferenceResolverHolder.reset();
        }
        assertNotNull(result.getStudents().get(0));
        assertSame(result.getStudents().get(0), result.getDepartments().get(0).getStudents().get(0));

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(universityDTO));
        System.out.println("######################");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
    }
}