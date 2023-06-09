package com.demo.Employee.service;


import com.demo.Employee.model.Address;
import com.demo.Employee.repository.AddressDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
public class AddressServiceTest {

    @MockBean
    private AddressDAO addressDAO;

    @Autowired
    private AddressService addressService;

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


    @AfterEach
    void afterEach() {
        Mockito.reset(addressDAO);
    }

    @Test
    @DisplayName("test get address")
    void testGetAddress() {

        Address address = new Address();
        address.setZipCode(zipCode);
        address.setAptSuite(aptSuite);
        address.setAddressLine1(line1);
        address.setAddressLine2(line2);
        address.setSociety(society);
        address.setCity(city);
        address.setCountry(country);

        //mock find by id
        when(addressDAO.findById(1)).thenReturn(Optional.of(address));

        Address foundAddress = addressService.getAddressById(1);
        assertNotNull(foundAddress);
        assertEquals(foundAddress.getAddressLine2(), line2);
        assertEquals(foundAddress.getCity(), city);
        assertEquals(foundAddress.getAptSuite(), aptSuite);
        assertEquals(foundAddress.getZipCode(), zipCode);
    }

    @Test
    @DisplayName("test get non existing address")
    void testGetNonExistingAddress() {

        //mock non existing address
        when(addressDAO.findById(1)).thenReturn(Optional.empty());

        Address address = addressService.getAddressById(1);
        assertNull(address);
    }

    @Test
    @DisplayName("test update non existing address")
    void testUpdateNonExistingAddress() {

        Address address = new Address();

        //mock non existing address
        when(addressDAO.findById(1)).thenReturn(Optional.empty());

        Address updatedAddress = addressService.updateAddress(1, address);
        assertNull(updatedAddress);
    }

    @Test
    @DisplayName("test update address")
    void testUpdateAddress() {

        Address existingAddress = new Address();
        existingAddress.setZipCode(zipCode);
        existingAddress.setAptSuite(aptSuite);
        existingAddress.setAddressLine1(line1);
        existingAddress.setAddressLine2(line2);
        existingAddress.setSociety(society);
        existingAddress.setCity(city);
        existingAddress.setCountry(country);

        Address addressToUpdate = new Address();
        addressToUpdate.setZipCode(updatedZipCode);
        addressToUpdate.setAptSuite(updatedAptSuite);
        addressToUpdate.setAddressLine1(line1);
        addressToUpdate.setAddressLine2(line2);
        addressToUpdate.setSociety(society);
        addressToUpdate.setCity(updatedCity);
        addressToUpdate.setCountry(country);

        //mock non existing address
        when(addressDAO.findById(1)).thenReturn(Optional.of(existingAddress));

        //mock save address
        when(addressDAO.save(any(Address.class))).thenReturn(addressToUpdate);

        Address updatedAddress = addressService.updateAddress(1, addressToUpdate);

        assertNotNull(updatedAddress);
        assertEquals(updatedAddress.getZipCode(), updatedZipCode);
        assertEquals(updatedAddress.getAptSuite(), updatedAptSuite);
        assertEquals(updatedAddress.getCity(), updatedCity);
    }

}
