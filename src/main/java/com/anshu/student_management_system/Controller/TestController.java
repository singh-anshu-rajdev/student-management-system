package com.anshu.student_management_system.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/call")
    public ResponseEntity<String> testCall() {
        return ResponseEntity.ok("This is a test call");
    }

    @GetMapping("/login")
    public ResponseEntity<String> testLogin() {
        return ResponseEntity.ok("This is a test login call");
    }
}
