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

    @DeleteMapping
    public String deleteEmployee(@RequestBody EmployeeDto employeeDto) {
        try {
            try (Connection connection = FactoryConfig.getInstance().getConnection()) {
                connection.setAutoCommit(false);
                String sql = "DELETE FROM Employee WHERE empId = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, employeeDto.getEmpId());
                    int rowsDeleted = preparedStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        connection.commit();
                        return "Employee deleted successfully!";
                    } else {
                        connection.rollback();
                        return "Employee with ID " + employeeDto.getEmpId() + " not found.";
                    }
                } catch (SQLException e) {
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
        }
        return "Error deleting employee.";
    }


    @GetMapping(value = "/empId")
    public String getEmployee(@RequestBody EmployeeDto employeeDto) {
//        return employeeDto;
        return null;
    }

    @PutMapping
    public String updateEmployee(@RequestBody EmployeeDto employeeDto) {
        try {
            try (Connection connection = FactoryConfig.getInstance().getConnection()) {
                connection.setAutoCommit(false);
                String sql = "UPDATE Employee SET empName = ?, empEmail = ?, empDepartment = ?, empProfile = ? WHERE empId = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, employeeDto.getEmpName());
                    preparedStatement.setString(2, employeeDto.getEmpEmail());
                    preparedStatement.setString(3, employeeDto.getEmpDepartment());
                    preparedStatement.setBytes(4, employeeDto.getEmpProfile());
                    preparedStatement.setString(5, employeeDto.getEmpId());

                    int rowsUpdated = preparedStatement.executeUpdate();

                    if (rowsUpdated > 0) {
                        connection.commit();
                        return "Employee updated successfully!";
                    } else {
                        connection.rollback();
                        return "Employee with ID " + employeeDto.getEmpId() + " not found.";
                    }
                } catch (SQLException e) {
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
        }
        return "Error updating employee.";
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
