package com.demo.Employee.controller;

import com.demo.Employee.enums.EmpStatus;
import com.demo.Employee.enums.Gender;
import com.demo.Employee.model.Address;
import com.demo.Employee.model.Department;
import com.demo.Employee.model.Employee;
import com.demo.Employee.model.Qualification;
import com.demo.Employee.repository.EmployeeDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeDAO employeeDAO;

    private Address address;

    private Department department;

    private Qualification qualification;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Value("${mock.employee.first.name}")
    private String firstName;

    @Value("${mock.employee.last.name}")
    private String lastName;

    @Value("${mock.employee.age}")
    private Integer age;

    @Value("${mock.employee.email}")
    private String emailId;

    @Value("${mock.employee.updated.first.name}")
    private String updatedFirstName;

    @Value("${mock.employee.updated.last.name}")
    private String updatedLastName;

    @Value("${mock.employee.updated.age}")
    private Integer updatedAge;

    @Value("${mock.employee.updated.email}")
    private String updatedEmailId;

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

    @Value("${mock.address.updated.zip.code}")
    private Integer updatedZipCode;

    @Value("${mock.address.updated.city}")
    private String updatedCity;

    @Value("${mock.address.updated.suite}")
    private String updatedAptSuite;

    @Value("${mock.department.name}")
    private String departmentName;

    @Value("${mock.qualification.name}")
    private String qualificationName;

    @BeforeEach
    void beforeEach() {
        address = new Address();
        address.setZipCode(zipCode);
        address.setAptSuite(aptSuite);
        address.setAddressLine1(line1);
        address.setAddressLine2(line2);
        address.setSociety(society);
        address.setCity(city);
        address.setCountry(country);

        department = new Department();
        department.setValue(departmentName);

        qualification = new Qualification();
        qualification.setValue(qualificationName);

    }

    @AfterEach
    void afterEach() {
        address = null;
        department = null;
        qualification = null;
        Mockito.reset(employeeDAO);
    }

    @Test
    @DisplayName("Test Get All Employees")
    public void testGetEmployeesRequest() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);

        when(employeeDAO.findAll()).thenReturn(employeeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Test Create An Employee")
    public void testCreateAnEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);

        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName("Test Update An Employee")
    public void testUpdateAnEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);

        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        Employee updateEmp = new Employee();
        updateEmp.setFirstName(updatedFirstName);
        updateEmp.setLastName(updatedLastName);
        updateEmp.setEmailId(updatedEmailId);
        updateEmp.setAge(updatedAge);
        updateEmp.setGender(Gender.FEMALE);

        when(employeeDAO.save(any(Employee.class))).thenReturn(updateEmp);

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updateEmp.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateEmp.getLastName())))
                .andExpect(jsonPath("$.emailId", is(updateEmp.getEmailId())))
                .andExpect(jsonPath("$.age", is(updateEmp.getAge())))
                .andExpect(jsonPath("$.gender", is(Gender.FEMALE.toString())));
    }

    @Test
    @DisplayName("Test Update A non existing Employee")
    public void testUpdateANonExistingEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);

        when(employeeDAO.findById(0)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName("Test Create An Employee with Address")
    public void testCreateAnEmployeeWithAddress() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);
        employee.setAddress(address);

        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
    }


    @Test
    @DisplayName("Test Create An Employee with Department")
    public void testCreateAnEmployeeWithDepartment() throws Exception {

        Department department = new Department();
        department.setValue("mock department");

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);
        employee.setDepartment(department);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName("Test Create An Employee with Qualification")
    public void testCreateAnEmployeeWithQualification() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);
        employee.setQualification(qualification);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName("Test Delete a non existing Employee")
    public void testDeleteANonExistingEmployee() throws Exception {

        when(employeeDAO.findById(0)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("Test Delete an existing Employee")
    public void testDeleteAnExistingEmployee() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);
        employee.setAddress(address);
        employee.setQualification(qualification);
        employee.setDepartment(department);

        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Test inactivate an employee")
    public void testInactivateAnEmployee() throws Exception {

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setAge(age);
        employee.setGender(Gender.MALE);
        employee.setStatus(EmpStatus.DISCONTINUED);

        when(employeeDAO.findById(1)).thenReturn(Optional.of(employee));

        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/1/inactivate")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status",is(EmpStatus.DISCONTINUED.toString())));
    }

    @Test
    @DisplayName("Test inactivate a non existing employee")
    public void testInactivateANonExistingEmployee() throws Exception {

        when(employeeDAO.findById(0)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/0/inactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

}
