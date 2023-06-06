package com.demo.Employee.service;

import com.demo.Employee.enums.EmpStatus;
import com.demo.Employee.enums.Gender;
import com.demo.Employee.model.Address;
import com.demo.Employee.model.Department;
import com.demo.Employee.model.Employee;
import com.demo.Employee.model.Qualification;
import com.demo.Employee.repository.EmployeeDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    public List<Employee> getEmployees() {
        Iterable<Employee> employeeIterable = employeeDAO.findAll();
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeIterable.forEach(employeeList::add);
        return employeeList;
    }

    public void createEmployee(String firstName,
                                   String lastName,
                                   Gender gender,
                                   String emailId,
                                   Department department,
                                   Address address,
                                   Qualification qualification,
                                   int age) {

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setGender(gender);
        employee.setEmailId(emailId);
        employee.setDepartment(department);
        employee.setAge(age);
        employee.setAddress(address);
        employee.setQualification(qualification);
        employee.setStatus(EmpStatus.ACTIVE);
        employeeDAO.save(employee);
    }

    public Employee getEmployeeInfo(Integer employeeId) {
        Optional<Employee> optEmp = employeeDAO.findById(employeeId);
        if(optEmp.isPresent()) {
            return optEmp.get();
        }
        return null;
    }

    public Employee updateEmployeeInfo(Integer empId, Employee employee) {
        Optional<Employee> optEmp = employeeDAO.findById(empId);
        if(!optEmp.isPresent()) {
            return null;
        }
        Employee newEmp = optEmp.get();
        newEmp.setFirstName(employee.getFirstName());
        newEmp.setLastName(employee.getLastName());
        newEmp.setEmailId(employee.getEmailId());
        return employeeDAO.save(newEmp);
    }

    public Employee changeStatus(Integer employeeId, EmpStatus status) {
        Optional<Employee> optEmp = employeeDAO.findById(employeeId);
        if (!optEmp.isPresent()) {
            return null;
        }

        Employee updatedEmp = optEmp.get();
        updatedEmp.setStatus(status);
        return employeeDAO.save(updatedEmp);
    }
}
