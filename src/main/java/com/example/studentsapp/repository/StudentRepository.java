package com.example.studentsapp.repository;

import com.example.studentsapp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

boolean existsStudentByPhoneNumber(String phoneNumber);


}
