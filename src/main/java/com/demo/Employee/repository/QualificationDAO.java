package com.demo.Employee.repository;

import com.demo.Employee.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationDAO extends JpaRepository<Qualification, Integer> {

}
