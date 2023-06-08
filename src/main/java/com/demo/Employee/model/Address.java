package com.demo.Employee.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "address")
public class Address {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer zipCode;

    @Column
    private String addressLine1;

    @Column
    private String addressLine2;

    @Column
    private String aptSuite;

    @Column()
    private  String society;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String country;

}
