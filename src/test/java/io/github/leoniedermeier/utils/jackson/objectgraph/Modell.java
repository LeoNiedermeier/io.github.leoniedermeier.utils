package io.github.leoniedermeier.utils.jackson.objectgraph;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.github.leoniedermeier.utils.mapstruct.Identifiable;

public class Modell {

    public static class Department {
        String name;
        @JsonIdentityReference(alwaysAsId = true)
        List<Student> students = new ArrayList<Modell.Student>();

        public void addStudent(Student student) {
            students.add(student);
        }

        public String getName() {
            return name;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        public void setStudents(List<Student> students) {
            this.students = students;
        }
    }

    @JsonIdentityInfo(generator=ObjectIdGenerators.UUIDGenerator.class, property="my-id")
    public static class Student implements Identifiable<String> {

        String id;

        @Override
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public static class University {
        String name;

        List<Department> departments = new ArrayList<Modell.Department>();
        List<Student> students = new ArrayList<Modell.Student>();

        public List<Department> getDepartments() {
            return departments;
        }

        public String getName() {
            return name;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setDepartments(List<Department> departments) {
            this.departments = departments;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }
    }
}
