package com.demo.Employee.service;


import com.demo.Employee.enums.EmpStatus;
import com.demo.Employee.enums.Gender;
import com.demo.Employee.model.Address;
import com.demo.Employee.model.Department;
import com.demo.Employee.model.Employee;
import com.demo.Employee.model.Qualification;
import com.demo.Employee.repository.EmployeeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class EmployeeServiceTest {

    @MockBean
    private EmployeeDAO employeeDAO;

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private QualificationService qualificationService;

    @MockBean
    private AddressService addressService;

    private Department department;

    private Qualification qualification;

    private Address address;

    private Employee employee;

    @Value("${mock.employee.first.name}")
    private String firstName;

    @Value("${mock.employee.last.name}")
    private String lastName;

    @Value("${mock.employee.age}")
    private int age;

    @Value("${mock.department.name}")
    private String departmentName;

    @Value("${mock.department.update}")
    private String updatedDepartmentName;

    @Value("${mock.qualification.name}")
    private String qualificationName;

    @Value("${mock.qualification.update}")
    private String updatedQualificationName;

    @Value("${mock.address.line1}")
    private String line1;

    @Value("${mock.address.line2}")
    private String line2;

    @Value("${mock.address.zip.code}")
    private Integer zipCode;

    @Value("${mock.address.city}")
    private String city;

    @Value("${mock.address.suite}")
    private String aptSuite;

    @Value("${mock.address.society}")
    private String society;

    @Value("${mock.address.country}")
    private String country;

    @BeforeEach
    void beforeEach() {
        department = new Department();
        department.setId(1);
        department.setValue(departmentName);

        qualification = new Qualification();
        qualification.setId(1);
        qualification.setValue(qualificationName);

        address = new Address();
        address.setId(1);
        address.setZipCode(zipCode);
        address.setAptSuite(aptSuite);
        address.setAddressLine1(line1);
        address.setAddressLine2(line2);
        address.setSociety(society);
        address.setCity(city);
        address.setCountry(country);

        employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setGender(Gender.MALE);
        employee.setAge(age);
        employee.setDepartment(department);
        employee.setQualification(qualification);
        employee.setAddress(address);
    }

    @Test
    @DisplayName("test change status")
    void testChangeStatus() {

        //mock find by id
        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        employee.setStatus(EmpStatus.DISCONTINUED);

        //mock save object
        when(employeeDAO.save(employee)).thenReturn(employee);

        Employee updatedEmp = employeeService.changeStatus(1, EmpStatus.DISCONTINUED);
        assertNotNull(updatedEmp);
        assertEquals(updatedEmp.getStatus(), EmpStatus.DISCONTINUED);
    }

    @Test
    @DisplayName("test change status for non existing employee")
    void testChangeStatusForNonExistingEmployee() {
        //mock find by id
        when(employeeDAO.findById(1)).thenReturn(Optional.empty());

        Employee updatedEmp = employeeService.changeStatus(1, EmpStatus.DISCONTINUED);
        assertNull(updatedEmp);
    }

    @Test
    @DisplayName("test get employee info")
    void testGetEmployeeInfo() {

        //mock find by id
        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        Employee foundEmp = employeeService.getEmployeeInfo(1);

        assertNotNull(foundEmp);
        assertEquals(foundEmp.getAge(), age);
    }
    @Test
    @DisplayName("test get non existing employee info")
    void testGetNonExistingEmployeeInfo() {

        //mock find by id
        when(employeeDAO.findById(1)).thenReturn(Optional.empty());

        Employee foundEmp = employeeService.getEmployeeInfo(1);

        assertNull(foundEmp);
    }

    @Test
    @DisplayName("test create an employee")
    void testCreateAnEmployee() {

        //mock employee save
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        //mock create department
        when(departmentService.createDepartment(departmentName)).thenReturn(department);

        //mock create qualification
        when(qualificationService.createQualification(qualificationName)).thenReturn(qualification);

        //mock create address
        when(addressService.createAddress(address)).thenReturn(address);

        Employee createdEmp = employeeService.createEmployee(employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                employee.getEmailId(),
                employee.getDepartment(),
                employee.getAddress(),
                employee.getQualification(),
                employee.getAge());

        assertNotNull(createdEmp);

        assertNotNull(createdEmp.getDepartment());
        assertEquals(createdEmp.getDepartment().getId(), 1);
        assertNotNull(createdEmp.getQualification());
        assertEquals(createdEmp.getQualification().getId(), 1);
        assertNotNull(createdEmp.getAddress());
        assertEquals(createdEmp.getAddress().getId(), 1);
    }


    @Test
    @DisplayName("test create an employee with existing Ids")
    void testCreateAnEmployeeWithExistingIds() {

        //mock employee save
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        //mock get department
        when(departmentService.getDepartment(department.getId())).thenReturn(department);

        //mock get qualification
        when(qualificationService.getQualificationInfo(qualification.getId())).thenReturn(qualification);

        //mock get address
        when(addressService.getAddressById(address.getId())).thenReturn(address);

        Employee createdEmp = employeeService.createEmployee(employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                employee.getEmailId(),
                employee.getDepartment(),
                employee.getAddress(),
                employee.getQualification(),
                employee.getAge());

        assertNotNull(createdEmp);
        assertEquals(createdEmp.getEmailId(), employee.getEmailId());
        assertNotNull(createdEmp.getDepartment());
        assertNotNull(createdEmp.getQualification());
        assertNotNull(createdEmp.getAddress());
    }

    @Test
    @DisplayName("test create an employee with null Ids")
    void testCreateAnEmployeeWithNullIds() {

        //mock employee save
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        //mock create department
        department.setId(null);
        when(departmentService.createDepartment(departmentName)).thenReturn(department);

        //mock create qualification
        qualification.setId(null);
        when(qualificationService.createQualification(qualificationName)).thenReturn(qualification);

        //mock create address
        address.setId(null);
        when(addressService.createAddress(address)).thenReturn(address);

        Employee createdEmp = employeeService.createEmployee(employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                employee.getEmailId(),
                employee.getDepartment(),
                employee.getAddress(),
                employee.getQualification(),
                employee.getAge());

        assertNotNull(createdEmp);

        assertNotNull(createdEmp.getDepartment());
        assertNotNull(createdEmp.getQualification());
        assertNotNull(createdEmp.getAddress());
    }

    @Test
    @DisplayName("test create an employee with null objects")
    void testCreateAnEmployeeWithNullObjects() {

        //mock employee save
        employee.setDepartment(null);
        employee.setQualification(null);
        employee.setAddress(null);

        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        Employee createdEmp = employeeService.createEmployee(employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                employee.getEmailId(),
                null,
                null,
                null,
                employee.getAge());

        assertNotNull(createdEmp);
        assertNull(createdEmp.getDepartment());
        assertNull(createdEmp.getQualification());
        assertNull(createdEmp.getAddress());
    }

    @Test
    @DisplayName("test update employee")
    void testUpdateEmployee() {

        //mock find by id
        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        //mock employee save
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        //mock create department
        when(departmentService.createDepartment(departmentName)).thenReturn(department);

        //mock create qualification
        when(qualificationService.createQualification(qualificationName)).thenReturn(qualification);

        //mock create address
        when(addressService.createAddress(address)).thenReturn(address);

        Employee updatedEmp = employeeService.updateEmployeeInfo(1, employee);
        assertNotNull(updatedEmp);

        assertNotNull(updatedEmp.getDepartment());
        assertEquals(updatedEmp.getDepartment().getId(), 1);
        assertNotNull(updatedEmp.getQualification());
        assertEquals(updatedEmp.getQualification().getId(), 1);
        assertNotNull(updatedEmp.getAddress());
        assertEquals(updatedEmp.getAddress().getId(), 1);
    }

    @Test
    @DisplayName("test update employee with null Objects")
    void testUpdateEmployeeWithNullObjects() {

        //mock find by id
        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        //mock employee save
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        //mock create department
        employee.setDepartment(null);
        when(departmentService.createDepartment(departmentName)).thenReturn(department);

        //mock create qualification
        employee.setQualification(null);
        when(qualificationService.createQualification(qualificationName)).thenReturn(qualification);

        //mock create address
        employee.setAddress(null);
        when(addressService.createAddress(address)).thenReturn(address);

        Employee inputEmp = new Employee();
        inputEmp.setFirstName(firstName);
        inputEmp.setLastName(lastName);
        inputEmp.setGender(Gender.MALE);
        inputEmp.setAge(age);

        Employee updatedEmp = employeeService.updateEmployeeInfo(1, inputEmp);
        assertNotNull(updatedEmp);
    }

    @Test
    @DisplayName("test update employee with null Ids")
    void testUpdateEmployeeWithNullIds() {

        //mock find by id
        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        //mock employee save
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        //mock create department
        department.setId(null);
        when(departmentService.createDepartment(departmentName)).thenReturn(department);

        //mock create qualification
        qualification.setId(null);
        when(qualificationService.createQualification(qualificationName)).thenReturn(qualification);

        //mock create address
        address.setId(null);
        when(addressService.createAddress(address)).thenReturn(address);

        Employee updatedEmp = employeeService.updateEmployeeInfo(1, employee);
        assertNotNull(updatedEmp);

        assertNotNull(updatedEmp.getDepartment());
        assertNotNull(updatedEmp.getQualification());
        assertNotNull(updatedEmp.getAddress());
    }

    @Test
    @DisplayName("test update employee with existing Ids")
    void testUpdateEmployeeWithExistingIds() {

        //mock find by id
        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        //mock employee save
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        //mock get department
        when(departmentService.getDepartment(department.getId())).thenReturn(department);

        //mock get qualification
        when(qualificationService.getQualificationInfo(qualification.getId())).thenReturn(qualification);

        //mock get address
        when(addressService.getAddressById(address.getId())).thenReturn(address);

        Employee updatedEmp = employeeService.updateEmployeeInfo(1, employee);
        assertNotNull(updatedEmp);

        assertNotNull(updatedEmp.getDepartment());
        assertEquals(updatedEmp.getDepartment().getId(), 1);
        assertNotNull(updatedEmp.getQualification());
        assertEquals(updatedEmp.getQualification().getId(), 1);
        assertNotNull(updatedEmp.getAddress());
        assertEquals(updatedEmp.getAddress().getId(), 1);
    }
}
