package com.example.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    private String empId;
    private String empName;
    private String empEmail;
    private String empDepartment;
    private byte[] empProfile;
}
