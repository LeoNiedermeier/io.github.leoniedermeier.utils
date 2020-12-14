package io.github.leoniedermeier.utils.mapstruct.example;

import java.util.ArrayList;
import java.util.List;

import io.github.leoniedermeier.utils.mapstruct.Identifiable;

public class Modell {

    public static class Department {
        String name;
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

class DTOs {

    public static class DepartmentDTO {
        String name;

        List<String> studentIds = new ArrayList<>();

        public void addStudentId(String student) {
            studentIds.add(student);
        }

        public String getName() {
            return name;
        }

        public List<String> getStudentIds() {
            return studentIds;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class StudentDTO implements Identifiable<String> {
        String id;

        @Override
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class UniversityDTO {
        String name;

        List<DepartmentDTO> departments = new ArrayList<>();
        List<StudentDTO> students = new ArrayList<>();

        public List<DepartmentDTO> getDepartments() {
            return departments;
        }

        public String getName() {
            return name;
        }

        public List<StudentDTO> getStudents() {
            return students;
        }

        public void setDepartments(List<DepartmentDTO> departments) {
            this.departments = departments;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStudents(List<StudentDTO> students) {
            this.students = students;
        }
    }
}
