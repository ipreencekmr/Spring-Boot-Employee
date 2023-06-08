package com.demo.Employee.controller;

import com.demo.Employee.enums.EmpStatus;
import com.demo.Employee.model.Employee;
import com.demo.Employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity createEmployee(@RequestBody Employee employee) {
        employeeService.createEmployee(employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                employee.getEmailId(),
                employee.getDepartment(),
                employee.getAddress(),
                employee.getQualification(),
                employee.getAge());
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employeeList = employeeService.getEmployees();
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    @PutMapping("/employees/{empId}")
    public ResponseEntity<Employee> updateAnEmployee(@PathVariable String empId, @RequestBody Employee employee) {
        Integer employeeId = Integer.parseInt(empId);
        Employee updatedEmployee = employeeService.updateEmployeeInfo(employeeId, employee);
        if (updatedEmployee == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @PutMapping("employees/{empId}/inactivate")
    public ResponseEntity<Employee> inactivateAnEmployee(@PathVariable String empId) {
        Integer employeeId = Integer.parseInt(empId);
        Employee updatedEmployee = employeeService.changeStatus(employeeId, EmpStatus.DISCONTINUED);
        if (updatedEmployee == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("employees/{empId}")
    public ResponseEntity<Boolean> deleteAnEmployee(@PathVariable String empId) {
        Integer employeeId = Integer.parseInt(empId);
        Boolean deleted = employeeService.deleteAnEmployee(employeeId);

        if (deleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

}
