package com.example.desafio.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "TB_PERSON")
public class PersonModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String specie;
    @Column(nullable = false)
    private String power;
    @Column(nullable = false)
    private String powerLevel;
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @OneToMany(mappedBy = "personModel")
    @JsonManagedReference
    private List<LogModel> logModelList;


}
