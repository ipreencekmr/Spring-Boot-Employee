package com.demo.Employee.repository;

import com.demo.Employee.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDAO extends JpaRepository<Address, Integer> {

}
