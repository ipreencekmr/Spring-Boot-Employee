package com.demo.Employee.controller;

import com.demo.Employee.enums.Gender;
import com.demo.Employee.model.Employee;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                                        .andExpect(jsonPath("$", hasSize(5)));
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
