package io.github.leoniedermeier.utils.jackson.objectgraph;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.leoniedermeier.utils.jackson.objectgraph.Modell.Department;
import io.github.leoniedermeier.utils.jackson.objectgraph.Modell.Student;
import io.github.leoniedermeier.utils.jackson.objectgraph.Modell.University;

class ObjectGraphWithIdTest {

    @Test
    void test() throws Exception {
        Student student = new Student();
        student.setId("myName");

        University university = new University();
        Department department = new Department();
        department.setName("department");
        university.departments.add(department);

        // Same Student instance in two different collections:
        university.students.add(student);
        department.students.add(student);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(university);
       // System.out.println(json);
        University readValue = mapper.readValue(json, University.class);
        //System.out.println(readValue);
        // After deserialization: 
        // Expect that the two Student instances are the same instance / same object
        assertSame(readValue.getStudents().get(0), readValue.getDepartments().get(0).getStudents().get(0));
    }

}
