package com.demo.Employee.controller;

import com.demo.Employee.model.Qualification;
import com.demo.Employee.service.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QualificationController {

    @Autowired
    private QualificationService qualificationService;

    @PostMapping("/qualifications")
    public ResponseEntity<Qualification> createQualification(@RequestBody Qualification qualification) {
        Qualification qlf = qualificationService.createQualification(qualification.getValue());
        return new ResponseEntity<>(qlf, HttpStatus.CREATED);
    }

    @PutMapping("qualifications/{qlfId}")
    public ResponseEntity<Qualification> updateQualification(@PathVariable String qlfId,
                                                             @RequestBody Qualification qualification) {

        Integer qualificationId = Integer.parseInt(qlfId);
        Qualification qlf = qualificationService.updateQualification(qualificationId, qualification);
        if (qlf == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(qlf, HttpStatus.OK);
    }

    @GetMapping("qualifications")
    public ResponseEntity<List<Qualification>> getQualifications() {
        List<Qualification> qlfList = qualificationService.getAllQualifications();
        return new ResponseEntity<>(qlfList, HttpStatus.OK);
    }

    @GetMapping("qualifications/{qlfId}")
    public ResponseEntity<Qualification> getQualificationInfo(@PathVariable String qlfId) {
        Integer qualificationId = Integer.parseInt(qlfId);
        Qualification qlf = qualificationService.getQualificationInfo(qualificationId);
        if (qlf == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(qlf, HttpStatus.OK);
    }
}
