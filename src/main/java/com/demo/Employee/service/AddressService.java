package com.demo.Employee.service;

import com.demo.Employee.model.Address;
import com.demo.Employee.repository.AddressDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    AddressDAO addressDAO;

    public Address createAddress(Address address) {
        return addressDAO.save(address);
    }

    public Address getAddressById(Integer addressId) {
        Optional<Address> optAddr = addressDAO.findById(addressId);
        if (optAddr.isPresent()) {
            return optAddr.get();
        }
        return null;
    }

    public Address updateAddress(Integer addressId, Address address) {
        Optional<Address> optAddr = addressDAO.findById(addressId);
        if (!optAddr.isPresent()) {
            return null;
        }

        Address updatedAddress = optAddr.get();
        updatedAddress.setAddressLine1(address.getAddressLine1());
        updatedAddress.setAddressLine2(address.getAddressLine2());
        updatedAddress.setZipCode(address.getZipCode());
        updatedAddress.setAptSuite(address.getAptSuite());
        updatedAddress.setSociety(address.getSociety());
        updatedAddress.setCountry(address.getCountry());
        updatedAddress.setCity(address.getCity());

        return addressDAO.save(updatedAddress);
    }
}
