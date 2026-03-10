package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    // Create or Update Student
    public Student saveStudent(Student student) {
        // Check for duplicate email
        if (student.getId() == null) { // New student
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new RuntimeException("Email already registered: " + student.getEmail());
            }
        } else { // Updating existing student
            Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
            if (existingStudent.isPresent() && !existingStudent.get().getId().equals(student.getId())) {
                throw new RuntimeException("Email already registered by another student: " + student.getEmail());
            }
        }
        return studentRepository.save(student);
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    // Get student by ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }
    
    // Get students by course
    public List<Student> getStudentsByCourse(String course) {
        return studentRepository.findByCourse(course);
    }
    
    // Get students by name (search)
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Update student
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudentById(id);
        
        // Check email uniqueness if it's being changed
        if (!student.getEmail().equals(studentDetails.getEmail())) {
            if (studentRepository.existsByEmail(studentDetails.getEmail())) {
                throw new RuntimeException("Email already registered: " + studentDetails.getEmail());
            }
        }
        
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        student.setCourse(studentDetails.getCourse());
        
        return studentRepository.save(student);
    }
    
    // Delete student
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
    
    // Check if student exists
    public boolean studentExists(Long id) {
        return studentRepository.existsById(id);
    }
    
    // Get student count
    public long getStudentCount() {
        return studentRepository.count();
    }
}