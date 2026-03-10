package com.example.studentmanagement.repository;
import com.example.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find student by email (exact match)
    Optional<Student> findByEmail(String email);
    
    // Find students by course (exact match)
    List<Student> findByCourse(String course);
    
    // Find students by course containing text (case insensitive)
    List<Student> findByCourseContainingIgnoreCase(String course);
    
    // Find students by name containing text
    List<Student> findByNameContainingIgnoreCase(String name);
    
    // Custom query to check if email exists
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.email = :email")
    boolean existsByEmail(@Param("email") String email);
    
    // Custom query to find students by course with native SQL
    @Query(value = "SELECT * FROM students WHERE course = :course", nativeQuery = true)
    List<Student> findStudentsByCourseNative(@Param("course") String course);
}