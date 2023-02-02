package com.example.desafio.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonDto {

    @NotBlank
    private String name;
    @NotNull
    private int age;
    @NotBlank
    private String specie;
    @NotBlank
    private String power;
    @NotNull
    private String powerLevel;
    @NotBlank
    private String email;
}
