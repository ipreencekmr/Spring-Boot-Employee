package com.demo.Employee.service;

import com.demo.Employee.model.Qualification;
import com.demo.Employee.repository.QualificationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QualificationService {

    @Autowired
    QualificationDAO qualificationDAO;

    public Qualification createQualification(String value) {
        Qualification qualification = new Qualification();
        qualification.setValue(value);
        return qualificationDAO.save(qualification);
    }

    public Boolean deleteQualification(Integer qualificationId) {
        Optional<Qualification> optQlf = qualificationDAO.findById(qualificationId);
        if (!optQlf.isPresent()) {
            return false;
        }
        qualificationDAO.delete(optQlf.get());
        return true;
    }

    public Qualification updateQualification(Integer qualificationId, Qualification qualification) {
        Optional<Qualification> optQlf = qualificationDAO.findById(qualificationId);
        if (!optQlf.isPresent()) {
            return null;
        }
        Qualification qlf = optQlf.get();
        qlf.setValue(qualification.getValue());
        return qualificationDAO.save(qlf);
    }

    public Qualification getQualificationInfo(Integer qualificationId) {
        Optional<Qualification> optQlf = qualificationDAO.findById(qualificationId);
        if (!optQlf.isPresent()) {
            return null;
        }
        return optQlf.get();
    }

    public List<Qualification> getAllQualifications() {
        return qualificationDAO.findAll();
    }
}
