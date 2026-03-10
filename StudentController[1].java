package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    void Attribute1(Model model){
        model.addAttribute("student", new Student());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("totalStudents", studentService.getStudentCount());
    }//This is the DRY principle 101 over here
    // Home page - Display form and all students
    @GetMapping("/")
    public String home(Model model) {
        Attribute1(model);
        return "index";
    }
    
    // Add new student
    @PostMapping("/api/students")
    public String addStudent(@Valid @ModelAttribute("student") Student student,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            Attribute1(model);
            return "index";
        }
        
        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/";
    }
    
    // Show edit form
    @GetMapping("/student/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Student student = studentService.getStudentById(id);
            model.addAttribute("student", student);
            Attribute1(model);
            return "index";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }
    
    // Update student
    @PostMapping("/api/students/{id}")
    public String updateStudent(@PathVariable Long id,
                               @Valid @ModelAttribute("student") Student student,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            Attribute1(model);
            return "index";
        }
        
        try {
            studentService.updateStudent(id, student);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/";
    }
    
    // Delete student
    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/";
    }
    
    // Search students by name
    @GetMapping("/student/search")
    public String searchStudents(@RequestParam(required = false) String name, Model model) {
        if (name != null && !name.trim().isEmpty()) {
            model.addAttribute("students", studentService.searchStudentsByName(name));
            model.addAttribute("searchTerm", name);
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        model.addAttribute("student", new Student());
        model.addAttribute("totalStudents", studentService.getStudentCount());
        return "index";
    }
    
    // REST API Endpoints
    
    @GetMapping("/api/students")
    @ResponseBody
    public List<Student> getAllStudentsApi() {
        return studentService.getAllStudents();
    }
    
    @GetMapping("/api/students/{id}")
    @ResponseBody
    public Student getStudentByIdApi(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
    
    @GetMapping("/api/students/course/{course}")
    @ResponseBody
    public List<Student> getStudentsByCourseApi(@PathVariable String course) {
        return studentService.getStudentsByCourse(course);
    }
    
    @PostMapping("/api/students/add")
    @ResponseBody
    public Student addStudentApi(@Valid @RequestBody Student student) {
        return studentService.saveStudent(student);
    }
    
    @DeleteMapping("/api/students/{id}")
    @ResponseBody
    public String deleteStudentApi(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "Student deleted successfully with id: " + id;
    }
}