package com.demo.Employee.controller;
import com.demo.Employee.model.Qualification;
import com.demo.Employee.repository.QualificationDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class QualificationControllerTest {

    @MockBean
    private QualificationDAO qualificationDAO;

    private static MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${mock.qualification.name}")
    private String qualificationName;

    @Value("${mock.qualification.update}")
    private String updatedQualificationName;

    @BeforeEach
    void beforeEach() {

        Qualification qualification = new Qualification();
        qualification.setValue(qualificationName);

        List<Qualification> qualificationList = new ArrayList<>();
        qualificationList.add(qualification);

        //mock get all qualifications
        when(qualificationDAO.findAll()).thenReturn(qualificationList);

        //mock create qualification
        when(qualificationDAO.save(qualification)).thenReturn(qualification);

        //mock get qualification info
        qualification.setId(1);
        when(qualificationDAO.findById(1)).thenReturn(of(qualification));

        //mock get qualification info with no id
        when(qualificationDAO.findById(0)).thenReturn(empty());
    }

    @AfterEach
    void afterEach() {
        Mockito.reset(qualificationDAO);
    }

    @Test
    @DisplayName("test list qualifications")
    void testListQualifications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/qualifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("test create qualification")
    void testCreateQualification() throws Exception {

        Qualification qualification = new Qualification();
        qualification.setValue(qualificationName);

        mockMvc.perform(MockMvcRequestBuilders.post("/qualifications")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(qualification)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("test get qualification info")
    void testGetQualificationInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/qualifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(qualificationName)));
    }

    @Test
    @DisplayName("test get qualification info for non existing Id")
    void testGetNonExistingQualificationInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/qualifications/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("test update qualification with existing id")
    void testUpdateQualification() throws Exception {
        Qualification qualification = new Qualification();
        qualification.setId(1);
        qualification.setValue(updatedQualificationName);

        //mock update qualification
        when(qualificationDAO.save(qualification)).thenReturn(qualification);

        mockMvc.perform(MockMvcRequestBuilders.put("/qualifications/1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(qualification)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(updatedQualificationName)));
    }

    @Test
    @DisplayName("test update qualification with non existing id")
    void testUpdateNonExistingQualification() throws Exception {

        Qualification qualification = new Qualification();
        qualification.setValue(updatedQualificationName);

        //mock update qualification
        when(qualificationDAO.save(qualification)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/qualifications/0")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(qualification)))
                .andExpect(status().isNotFound());
    }

}
