package com.demo.Employee.service;

import com.demo.Employee.model.Department;
import com.demo.Employee.repository.DepartmentDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class DepartmentServiceTest {

    @MockBean
    private DepartmentDAO departmentDAO;

    @Autowired
    private DepartmentService departmentService;

    @Value("${mock.department.name}")
    private String departmentName;

    @Value("${mock.department.update}")
    private String updatedDepartmentName;

    @Test
    @DisplayName("Test delete department")
    void testDeleteDepartment() {
        Department department = new Department();
        department.setValue(departmentName);
        department.setId(1);

        //mock find by id to delete it
        when(departmentDAO.findById(1)).thenReturn(Optional.of(department));

        Boolean isDeleted = departmentService.deleteDepartment(1);
        assertEquals(isDeleted, true);
    }

    @Test
    @DisplayName("Test delete non existing department")
    void testDeleteNonExistingDepartment() {

        //mock find by id to delete it
        when(departmentDAO.findById(1)).thenReturn(Optional.empty());

        Boolean isDeleted = departmentService.deleteDepartment(1);
        assertEquals(isDeleted, false);
    }
}
