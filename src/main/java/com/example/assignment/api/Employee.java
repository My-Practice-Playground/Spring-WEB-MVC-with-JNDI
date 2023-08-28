package com.example.assignment.api;

import com.example.assignment.dto.EmployeeDto;
import com.example.assignment.util.FactoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
@RequestMapping("/emp")
public class Employee {

    @Autowired
    private FactoryConfig factoryConfig;

    public static <T> T execute(String sql, Object... args) throws SQLException, ClassNotFoundException, NamingException {
        InitialContext ctx = new InitialContext();
        DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/emp");
        Connection connection = pool.getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            pstm.setObject((i + 1), args[i]);
        }
        System.out.println(sql);
        if (sql.startsWith("SELECT") || sql.startsWith("select")) {
            return (T) pstm.executeQuery();
        }
        return (T) ((Boolean) (pstm.executeUpdate() > 0));   // convert boolean to Boolean(Boxing type)
    }

    @DeleteMapping
    public String deleteEmployee(@RequestBody EmployeeDto employeeDto) throws SQLException, NamingException, ClassNotFoundException {
        execute("DELETE FROM Employee WHERE empId = ?", employeeDto.getEmpId());
        return "Employee deleted successfully!";
    }

    /*@GetMapping(value = "/empId")
    public String getEmployee(@RequestBody EmployeeDto employeeDto) {

    }*/

    @PutMapping
    public String updateEmployee(@RequestBody EmployeeDto employee) throws NamingException, SQLException, ClassNotFoundException {
        execute("UPDATE Employee SET empName = ?, empEmail = ?, empDepartment = ?, empProfile = ? WHERE empId = ?", employee.getEmpId(), employee.getEmpName(), employee.getEmpEmail(), employee.getEmpDepartment(), employee.getEmpProfile());
        return "Employee updated successfully!";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String saveEmployeeData(@RequestParam String empId, @RequestParam String empName, @RequestParam String empEmail, @RequestParam String empDepartment, @RequestParam("profile") MultipartFile profileFile) throws IOException, SQLException, NamingException, ClassNotFoundException {
        byte[] profileBytes = profileFile.getBytes();
        //   String profileStr = Base64.getEncoder().encodeToString(profileBytes); //bytes to String
        EmployeeDto employee = new EmployeeDto(empId, empName, empEmail, empDepartment, profileBytes);
        execute("INSERT INTO Employee (empId, empName, empEmail, empDepartment, empProfile) VALUES (?, ?, ?, ?, ?)", employee.getEmpId(), employee.getEmpName(), employee.getEmpEmail(), employee.getEmpDepartment(), employee.getEmpProfile());
        return "Done";
    }
}
