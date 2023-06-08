package com.demo.Employee.controller;

import com.demo.Employee.enums.EmpStatus;
import com.demo.Employee.enums.Gender;
import com.demo.Employee.model.Address;
import com.demo.Employee.model.Department;
import com.demo.Employee.model.Employee;
import com.demo.Employee.model.Qualification;
import com.demo.Employee.repository.EmployeeDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class EmployeeControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @Autowired
    private EmployeeDAO employeeDAO;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Value("${sql.script.create.employee}")
    private String sqlAddEmployee;

    @Value("${sql.script.delete.employee}")
    private String sqlDeleteEmployee;

    @BeforeAll
    public static void beforeAll() {
        request = new MockHttpServletRequest();
        request.setParameter("firstName","MockFirstName");
        request.setParameter("lastName","MockLastName");
        request.setParameter("emailId", "MockEmail@mockdomain.com");
        request.setParameter("age", String.valueOf(35));
        request.setParameter("gender","MALE");
    }

    @BeforeEach
    void beforeEach() {
        jdbcTemplate.execute(sqlAddEmployee);
    }

    @Test
    @DisplayName("Test Get All Employees")
    public void testGetEmployeesRequest() throws Exception {

        employee = new Employee();
        employee.setFirstName("MockFirstName");
        employee.setLastName("MockLastName");
        employee.setEmailId("mockEmail@mockdomain.com");
        employee.setAge(25);
        employee.setGender(Gender.MALE);

        entityManager.persist(employee);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Test Create An Employee")
    public void testCreateAnEmployee() throws Exception {
        employee = new Employee();
        employee.setFirstName("MockFirstName");
        employee.setLastName("MockLastName");
        employee.setEmailId("mockEmail@mockdomain.com");
        employee.setAge(25);
        employee.setGender(Gender.MALE);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());

        Optional<Employee> emp = Optional.ofNullable(employeeDAO.findByEmailId("mockEmail@mockdomain.com"));
        assertTrue(emp.isPresent());

        Employee createdEmployee = emp.get();
        assertNotNull(createdEmployee);
        assertEquals(createdEmployee.getFirstName(), "MockFirstName");
        assertEquals(createdEmployee.getLastName(), "MockLastName");
        assertEquals(createdEmployee.getEmailId(), "mockEmail@mockdomain.com");
        assertEquals(createdEmployee.getAge(), 25);
        assertEquals(createdEmployee.getGender(), Gender.MALE);
        assertEquals(createdEmployee.getStatus(), EmpStatus.ACTIVE);
    }

    @Test
    @DisplayName("Test Update An Employee")
    public void testUpdateAnEmployee() throws Exception {
        employee = new Employee();
        employee.setFirstName("MockFirstName");
        employee.setLastName("MockLastName");
        employee.setEmailId("mockEmail@mockdomain.com");
        employee.setAge(25);
        employee.setGender(Gender.MALE);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());

        Optional<Employee> emp = Optional.ofNullable(employeeDAO.findByEmailId("mockEmail@mockdomain.com"));
        assertTrue(emp.isPresent());

        Employee createdEmployee = emp.get();

        //Update Information
        createdEmployee.setFirstName("modified first name");
        createdEmployee.setLastName("modified last name");
        createdEmployee.setEmailId("modifiedemail@mockdomain.com");
        createdEmployee.setAge(30);
        createdEmployee.setGender(Gender.FEMALE);

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/"+createdEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("modified first name")))
                .andExpect(jsonPath("$.lastName", is("modified last name")))
                .andExpect(jsonPath("$.emailId", is("modifiedemail@mockdomain.com")))
                .andExpect(jsonPath("$.age", is(30)))
                .andExpect(jsonPath("$.gender", is(Gender.FEMALE.toString())));
    }

    @Test
    @DisplayName("Test Update A non existing Employee")
    public void testUpdateANonExistingEmployee() throws Exception {
        employee = new Employee();
        employee.setFirstName("MockFirstName");
        employee.setLastName("MockLastName");
        employee.setEmailId("mockEmail@mockdomain.com");
        employee.setAge(25);
        employee.setGender(Gender.MALE);

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName("Test Create An Employee with Address")
    public void testCreateAnEmployeeWithAddress() throws Exception {

        Address address = new Address();
        address.setAddressLine1("mock line 1");
        address.setAddressLine2("mock line 2");
        address.setAptSuite("mock apt suite");
        address.setSociety("mock society");
        address.setCity("mock city");
        address.setState("mock state");
        address.setCountry("mock country");
        address.setZipCode(1001);

        employee = new Employee();
        employee.setFirstName("MockFirstName");
        employee.setLastName("MockLastName");
        employee.setEmailId("mockEmail@mockdomain.com");
        employee.setAge(25);
        employee.setGender(Gender.MALE);
        employee.setAddress(address);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());

        Optional<Employee> emp = Optional.ofNullable(employeeDAO.findByEmailId("mockEmail@mockdomain.com"));
        assertTrue(emp.isPresent());

        Employee createdEmployee = emp.get();

        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getAddress());
        assertEquals(createdEmployee.getAddress().getCity(), "mock city");
        assertEquals(createdEmployee.getAddress().getZipCode(), 1001);
        assertEquals(createdEmployee.getAddress().getAptSuite(), "mock apt suite");
        assertEquals(createdEmployee.getAddress().getAddressLine2(), "mock line 2");
    }


    @Test
    @DisplayName("Test Create An Employee with Department")
    public void testCreateAnEmployeeWithDepartment() throws Exception {

        Department department = new Department();
        department.setValue("mock department");

        employee = new Employee();
        employee.setFirstName("MockFirstName");
        employee.setLastName("MockLastName");
        employee.setEmailId("mockEmail@mockdomain.com");
        employee.setAge(25);
        employee.setGender(Gender.MALE);
        employee.setDepartment(department);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());

        Optional<Employee> emp = Optional.ofNullable(employeeDAO.findByEmailId("mockEmail@mockdomain.com"));
        assertTrue(emp.isPresent());

        Employee createdEmployee = emp.get();

        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getDepartment());
        assertNotNull(createdEmployee.getDepartment().getId());
        assertEquals(createdEmployee.getDepartment().getValue(), "mock department");
    }

    @Test
    @DisplayName("Test Create An Employee with Qualification")
    public void testCreateAnEmployeeWithQualification() throws Exception {

        Qualification qualification = new Qualification();
        qualification.setValue("mock qualification");

        employee = new Employee();
        employee.setFirstName("MockFirstName");
        employee.setLastName("MockLastName");
        employee.setEmailId("mockEmail@mockdomain.com");
        employee.setAge(25);
        employee.setGender(Gender.MALE);
        employee.setQualification(qualification);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());

        Optional<Employee> emp = Optional.ofNullable(employeeDAO.findByEmailId("mockEmail@mockdomain.com"));
        assertTrue(emp.isPresent());

        Employee createdEmployee = emp.get();

        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getQualification());
        assertNotNull(createdEmployee.getQualification().getId());
        assertEquals(createdEmployee.getQualification().getValue(), "mock qualification");
    }

    @Test
    @DisplayName("Test Delete a non existing Employee")
    public void testDeleteANonExistingEmployee() throws Exception {

        employee = new Employee();
        employee.setFirstName("MockFirstName");
        employee.setLastName("MockLastName");
        employee.setEmailId("mockEmail@mockdomain.com");
        employee.setAge(25);
        employee.setGender(Gender.MALE);

        entityManager.persist(employee);
        entityManager.flush();

        Optional<Employee> emp = Optional.ofNullable(employeeDAO.findByEmailId("mockEmail@mockdomain.com"));
        assertTrue(emp.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/"+emp.get().getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Test Delete an existing Employee")
    public void testDeleteAnExistingEmployee() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("false"));
    }

    @AfterEach
    void afterEach() {
        jdbcTemplate.execute(sqlDeleteEmployee);
    }


}
