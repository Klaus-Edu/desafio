package com.example.desafio.services;

import com.example.desafio.models.PersonModel;
import com.example.desafio.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public PersonModel save(PersonModel personModel) {
        return personRepository.save(personModel);
    }

    public boolean existsByName(String name) {
        return personRepository.existsByName(name);
    }

    public List<PersonModel> findAll() {
        return personRepository.findAll();
    }

    @Transactional
    public void delete(PersonModel personModel) {
        personRepository.delete(personModel);
    }


    public List<PersonModel> findByPowerLevel(String powerLevel) {
        return personRepository.findByPowerLevel(powerLevel);
    }

    public Optional<PersonModel> findById(Long id) {
        return personRepository.findById(id);
    }

    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }
}
