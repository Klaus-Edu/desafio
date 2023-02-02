package com.example.desafio.controllers;

import com.example.desafio.dtos.PersonDto;
import com.example.desafio.models.PersonModel;
import com.example.desafio.repositories.LogRepository;
import com.example.desafio.repositories.PersonRepository;
import com.example.desafio.services.LogService;
import com.example.desafio.services.MailService;
import com.example.desafio.services.PersonService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/person")
public class PersonController {

    final PersonService personService;

    private final PersonRepository personRepository;

    final LogService logService;

    private final LogRepository logRepository;

    final MailService mailService;

    @Autowired
    private JavaMailSender emailSender;

    public PersonController(PersonService personService, PersonRepository personRepository, LogService logService, LogRepository logRepository, MailService mailService) {
        this.personService = personService;
        this.personRepository = personRepository;
        this.logService = logService;
        this.logRepository = logRepository;
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<Object> savePerson(@RequestBody @Valid PersonDto personDto){
        PersonModel personModel = new PersonModel();

        BeanUtils.copyProperties(personDto,personModel);

        if(personService.existsByName(personDto.getName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Person already registered!");
        }
        if(personService.existsByEmail(personDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Person already registered!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(personModel));
    }

    @GetMapping
    public ResponseEntity<List<PersonModel>> getAllPeople(){
        return ResponseEntity.status(HttpStatus.FOUND).body(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPersonById(@PathVariable(value = "id") Long id){
        Optional<PersonModel> personModelOptional = personRepository.findById(id);

        if(!personModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not founded!");
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(personModelOptional.get());
        }
    }

    @GetMapping("/power/{powerLevel}")
    public ResponseEntity<List<PersonModel>> findPersonByPowerLevel(@PathVariable(value = "powerLevel") String powerLevel){
        return ResponseEntity.status(HttpStatus.FOUND).body(personService.findByPowerLevel(powerLevel));
    }

    @PutMapping("/edit/{id}")
    @Transactional
    public ResponseEntity<Object> editPersonById(@PathVariable(value = "id") Long id, @RequestBody @Valid PersonDto personDto){
        Optional<PersonModel> personModelOptional = personRepository.findById(id);

        if(!personModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Couldn't find this person!");
        } else {
            PersonModel personModel = personModelOptional.get();

            personModel.setName(personDto.getName());
            personModel.setAge(personDto.getAge());
            personModel.setSpecie(personDto.getSpecie());
            personModel.setPower(personDto.getPower());
            personModel.setPowerLevel(personDto.getPowerLevel());
            personModel.setEmail(personDto.getEmail());

            return ResponseEntity.status(HttpStatus.OK).body(personService.save(personModel));
        }
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Object> deletePersonById(@PathVariable(value = "id")Long id){
        Optional<PersonModel> personModelOptional = personRepository.findById(id);

        if(!personModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Couldn't find this person!");
        } else {
            personService.delete(personModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Person " + id + " successfully deleted!");
        }
    }

}
