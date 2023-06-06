package com.demo.Employee.service;

import com.demo.Employee.model.Department;
import com.demo.Employee.repository.DepartmentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentDAO departmentDAO;

    public Department createDepartment(String departmentName) {
        Department department = new Department();
        department.setValue(departmentName);
        return departmentDAO.save(department);
    }

    public List<Department> getAllDepartments() {
        List departments = departmentDAO.findAll();
        return departments;
    }

    public Department getDepartment(Integer departmentId) {
        Optional<Department> optDept = departmentDAO.findById(departmentId);
        if (optDept.isPresent()) {
            return optDept.get();
        }
        return null;
    }

    public Department updateDepartment(Integer departmentId, String departmentName) {
        Optional<Department> optDept = departmentDAO.findById(departmentId);
        if (!optDept.isPresent()) {
            return null;
        }
        Department updateDepartment = optDept.get();
        updateDepartment.setValue(departmentName);
        return departmentDAO.save(updateDepartment);
    }

    public Boolean deleteDepartment(Integer departmentId) {
        Optional<Department> optDept = departmentDAO.findById(departmentId);
        if (!optDept.isPresent()) {
            return false;
        }

        departmentDAO.delete(optDept.get());
        return true;
    }
}
