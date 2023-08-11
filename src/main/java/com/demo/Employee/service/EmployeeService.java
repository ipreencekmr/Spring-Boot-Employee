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

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private QualificationService qualificationService;

    @Autowired
    private AddressService addressService;

    public List<Employee> getEmployees() {
        Iterable<Employee> employeeIterable = employeeDAO.findAll();
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeIterable.forEach(employeeList::add);
        return employeeList;
    }

    public Employee createEmployee(String firstName,
                                   String lastName,
                                   Gender gender,
                                   String emailId,
                                   Department department,
                                   Address address,
                                   Qualification qualification,
                                   int age) {

        Department foundDept = department != null && department.getId() != null ? departmentService.getDepartment(department.getId()) : null;
        if(department != null && foundDept == null) {
            foundDept = departmentService.createDepartment(department.getValue());
        }

        Qualification foundQlf = qualification != null && qualification.getId() != null ? qualificationService.getQualificationInfo(qualification.getId()) : null;
        if (qualification != null && foundQlf == null) {
            foundQlf = qualificationService.createQualification(qualification.getValue());
        }

        Address foundAddr = address != null && address.getId() != null ? addressService.getAddressById(address.getId()) : null;
        if (address != null && foundAddr == null){
            foundAddr = addressService.createAddress(address);
        }

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setGender(gender);
        employee.setEmailId(emailId);
        employee.setDepartment(foundDept);
        employee.setAge(age);
        employee.setAddress(foundAddr);
        employee.setQualification(foundQlf);
        employee.setStatus(EmpStatus.ACTIVE);
        return employeeDAO.save(employee);
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
        newEmp.setAge(employee.getAge());
        newEmp.setGender(employee.getGender());

        Department department = employee.getDepartment();
        Qualification qualification = employee.getQualification();
        Address address = employee.getAddress();

        Department foundDept = department != null && department.getId() != null ? departmentService.getDepartment(department.getId()) : null;
        if(department != null && foundDept == null) {
            foundDept = departmentService.createDepartment(department.getValue());
        }

        Qualification foundQlf = qualification != null && qualification.getId() != null ? qualificationService.getQualificationInfo(qualification.getId()) : null;
        if (qualification != null && foundQlf == null) {
            foundQlf = qualificationService.createQualification(qualification.getValue());
        }

        Address foundAddr = address != null && address.getId() != null ? addressService.getAddressById(address.getId()) : null;
        if (address != null){
            if(foundAddr == null) {
                foundAddr = addressService.createAddress(address);
            }else {
                foundAddr = addressService.updateAddress(address.getId(), address);
            }
        }

        newEmp.setQualification(foundQlf);
        newEmp.setDepartment(foundDept);
        newEmp.setAddress(foundAddr);

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

    public Boolean deleteAnEmployee(Integer employeeId) {
        Optional<Employee> optEmp = employeeDAO.findById(employeeId);
        if (!optEmp.isPresent()) {
            return false;
        }
        employeeDAO.deleteById(employeeId);
        return true;
    }
}
