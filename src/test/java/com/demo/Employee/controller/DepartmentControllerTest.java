package com.demo.Employee.controller;

import com.demo.Employee.model.Department;
import com.demo.Employee.repository.DepartmentDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class DepartmentControllerTest {

    public static MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${sql.script.create.department}")
    private String sqlAddDepartment;

    @Value("${sql.script.delete.department}")
    private String sqlDeleteDepartment;

    @MockBean
    private DepartmentDAO departmentDAO;

    @Value("${mock.department.name}")
    private String departmentName;

    @Value("${mock.department.update}")
    private String updatedDepartmentName;

    @BeforeEach
    void beforeEach() {
        Department department = new Department();
        department.setValue(departmentName);
        Department returnedDepartment = new Department();
        returnedDepartment.setId(1);
        returnedDepartment.setValue(departmentName);

        //mock create department
        when(departmentDAO.save(department)).thenReturn(returnedDepartment);

        List<Department> departmentList = new ArrayList<>();
        departmentList.add(department);

        //mock list departments
        when(departmentDAO.findAll()).thenReturn(departmentList);

        //mock find department by id
        when(departmentDAO.findById(1)).thenReturn(Optional.of(department));

        //mock find department by non-existing id
        when(departmentDAO.findById(0)).thenReturn(Optional.empty());

        //mock update department by existing id
        department.setId(1);
        returnedDepartment.setValue(updatedDepartmentName);
        when(departmentDAO.save(department)).thenReturn(returnedDepartment);
    }

    @AfterEach
    void afterEach() {
        //reset DAO after each test
        Mockito.reset(departmentDAO);
    }

    @Test
    @DisplayName("test create department")
    void testCreateDepartment() throws Exception {

        Department department = new Department();
        department.setValue(departmentName);

        mockMvc.perform(MockMvcRequestBuilders.post("/departments")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("test list all departments")
    void testListAllDepartments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("test get department info")
    void testGetDepartmentInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(departmentName)));
    }

    @Test
    @DisplayName("test get department info for non existing id")
    void testGetNonExistingDepartmentInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/departments/0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName("test update existing department")
    void testUpdateExistingDepartment() throws Exception {

        Department department = new Department();
        department.setValue(departmentName);

        mockMvc.perform(MockMvcRequestBuilders.post("/departments")
               .contentType(APPLICATION_JSON_UTF8)
               .content(objectMapper.writeValueAsString(department)))
               .andExpect(status().isCreated());

        department.setValue(updatedDepartmentName);
        mockMvc.perform(MockMvcRequestBuilders.put("/departments/1")
                       .contentType(APPLICATION_JSON_UTF8)
               .content(objectMapper.writeValueAsString(department)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.value", is(updatedDepartmentName)));
    }

    @Test
    @DisplayName("test update non existing department")
    void testUpdateNonExistingDepartment() throws Exception {

        //mock update non existing department
        Department department = new Department();
        department.setId(0);
        when(departmentDAO.save(department)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/departments/0")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isNotFound());
    }

}
