package com.example.assignment.api;

import com.example.assignment.util.FactoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.util.Base64;

@RestController
@RequestMapping("/emp")
public class Employee {

    @Autowired
    private FactoryConfig factoryConfig;

    private Connection connection;

    @DeleteMapping
    public String deleteEmployee(@RequestBody com.example.assignment.dto.Employee employee) {
        System.out.println(employee);
        return "Done";
    }

    @GetMapping(value = "/empId")
    public String getEmployee(@RequestBody com.example.assignment.dto.Employee employee) {
//        return employee;
        return null;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String saveEmployeeData(@RequestParam String empId, @RequestParam String empName, @RequestParam String empEmail, @RequestParam String empDepartment, @RequestParam("profile") MultipartFile profileFile) throws IOException {

        byte[] profileBytes = profileFile.getBytes();
        String profileStr = Base64.getEncoder().encodeToString(profileBytes);

        com.example.assignment.dto.Employee employee = new com.example.assignment.dto.Employee();
        employee.setEmpId(empId);
        employee.setEmpName(empName);
        employee.setEmpEmail(empEmail);
        employee.setEmpDepartment(empDepartment);
        employee.setEmpProfile(profileBytes);
        return "Post : ";
    }

    @PostMapping
    String saveEmp(@RequestBody com.example.assignment.dto.Employee emp) {
        return "done";
    }
}
