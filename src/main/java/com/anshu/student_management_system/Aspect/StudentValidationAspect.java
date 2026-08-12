package com.anshu.student_management_system.Aspect;

import com.anshu.student_management_system.Entities.Student;
import com.anshu.student_management_system.ExceptionHandler.CustomValidationException;
import com.anshu.student_management_system.ExceptionHandler.ErrorCode;
import com.anshu.student_management_system.Repositories.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Aspect
@Component
public class StudentValidationAspect {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private HttpServletRequest request;


    @Around("@within(com.anshu.student_management_system.Annotations.ValidateStudent)")
    public Object validateStudent(ProceedingJoinPoint joinPoint) throws Throwable {

        String studentCode = request.getHeader("studentCode");
        String dateOfBirth = request.getHeader("dateOfBirth");

        if (studentCode == null || studentCode.isBlank()) {
            throw new CustomValidationException(ErrorCode.ERR_AP_2007);
        }

        if (dateOfBirth == null || dateOfBirth.isBlank()) {
            throw new CustomValidationException(ErrorCode.ERR_AP_2008);
        }

        LocalDate dob;
        try {
            dob = LocalDate.parse(dateOfBirth);
        } catch (Exception exception) {
            throw new CustomValidationException(ErrorCode.ERR_AP_2009);
        }
        Student student = studentRepository.findByStudentCodeAndDateOfBirth(studentCode,dob)
                .orElseThrow(() -> new CustomValidationException(ErrorCode.ERR_AP_2010));
        return joinPoint.proceed();
    }

}
