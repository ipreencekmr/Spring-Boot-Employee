package com.demo.Employee.service;


import com.demo.Employee.model.Qualification;
import com.demo.Employee.repository.DepartmentDAO;
import com.demo.Employee.repository.QualificationDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class QualificationServiceTest {

    @MockBean
    private QualificationDAO qualificationDAO;

    @Autowired
    private QualificationService qualificationService;

    @Value("${mock.qualification.name}")
    private String qualificationName;

    @Value("${mock.qualification.update}")
    private String updatedQualificationName;

    @Test
    @DisplayName("test delete qualification")
    void testDeleteQualification() {

        Qualification qualification = new Qualification();
        qualification.setValue(qualificationName);

        //mock find by id to perform delete
        when(qualificationDAO.findById(1)).thenReturn(Optional.of(qualification));

        Boolean isDeleted = qualificationService.deleteQualification(1);
        assertTrue(isDeleted);
    }

    @Test
    @DisplayName("test delete non existing qualification")
    void testDeleteNonExistingQualification() {

        //mock find by id to perform delete
        when(qualificationDAO.findById(1)).thenReturn(Optional.empty());

        Boolean isDeleted = qualificationService.deleteQualification(1);
        assertFalse(isDeleted);
    }

}
