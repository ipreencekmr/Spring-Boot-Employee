package com.demo.Employee.controller;

import com.demo.Employee.model.Department;
import com.demo.Employee.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @PostMapping(value = "/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {

       Department createdDepartment = departmentService.createDepartment(department.getValue());

        return new ResponseEntity(
                createdDepartment, HttpStatus.CREATED
        );
    }

    @GetMapping(value = "/departments")
    public ResponseEntity<List<Department>> getDepartmentList() {
        List<Department> departmentList = departmentService.getAllDepartments();
        return new ResponseEntity<>(departmentList, HttpStatus.OK);
    }

    @GetMapping(value = "/departments/{deptId}")
    public ResponseEntity<Department> getDepartmentInfo(@PathVariable String deptId) {
        Integer departmentId = Integer.parseInt(deptId);
        Department department = departmentService.getDepartment(departmentId);

        if (department == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PutMapping(value = "/departments/{deptId}")
    public ResponseEntity<Department> updateDepartment(@PathVariable String deptId,
                                                       @RequestBody Department department) {
        Integer departmentId = Integer.parseInt(deptId);
        Department updatedDepartment = departmentService.updateDepartment(departmentId, department.getValue());

        if(updatedDepartment == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }


    @DeleteMapping(value = "/departments/{deptId}")
    public ResponseEntity<Boolean> deleteDepartment(@PathVariable String deptId) {
        Integer departmentId = Integer.parseInt(deptId);
        Boolean isDeleted = departmentService.deleteDepartment(departmentId);
        if (isDeleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

}
