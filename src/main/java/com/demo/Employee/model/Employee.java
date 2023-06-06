package com.demo.Employee.model;
import com.demo.Employee.enums.EmpStatus;
import com.demo.Employee.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String emailId;

    @Column
    private int age;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private EmpStatus status;

    @ManyToOne
    @JoinColumn(name = "departmentId", referencedColumnName = "id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "addressId", referencedColumnName = "id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "qualificationId", referencedColumnName = "id")
    private Qualification qualification;
}
