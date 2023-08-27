package com.example.assignment.api;

import com.example.assignment.dto.EmployeeDto;
import com.example.assignment.util.FactoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

@RestController
@RequestMapping("/emp")
public class Employee {

    @Autowired
    private FactoryConfig factoryConfig;

    private Connection connection;

    @DeleteMapping
    public String deleteEmployee(@RequestBody EmployeeDto employeeDto) {
        return "Error deleting employee.";
    }


    @GetMapping(value = "/empId")
    public String getEmployee(@RequestBody EmployeeDto employeeDto) {
//        return employeeDto;
        return null;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String saveEmployeeData(@RequestParam String empId, @RequestParam String empName, @RequestParam String empEmail, @RequestParam String empDepartment, @RequestParam("profile") MultipartFile profileFile) throws IOException {

        byte[] profileBytes = profileFile.getBytes();
        String profileStr = Base64.getEncoder().encodeToString(profileBytes);
        EmployeeDto employeeDto = new EmployeeDto(empId, empName, empEmail, empDepartment, profileBytes);
        return saveEmployeeToDatabase(employeeDto);
    }

    private String saveEmployeeToDatabase(EmployeeDto employee) {
        try {
            try (Connection connection = FactoryConfig.getInstance().getConnection()) {
                connection.setAutoCommit(false);
                String sql = "INSERT INTO Employee (empId, empName, empEmail, empDepartment, empProfile) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, employee.getEmpId());
                    preparedStatement.setString(2, employee.getEmpName());
                    preparedStatement.setString(3, employee.getEmpEmail());
                    preparedStatement.setString(4, employee.getEmpDepartment());
                    preparedStatement.setBytes(5, employee.getEmpProfile());
                    preparedStatement.executeUpdate();
                    connection.commit();
                    return "Employee saved successfully!";
                } catch (SQLException e) {
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
        }
        return "Error! ";
    }
}
