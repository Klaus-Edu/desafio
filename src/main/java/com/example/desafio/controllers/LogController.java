package com.example.desafio.controllers;

import com.example.desafio.dtos.LogDto;
import com.example.desafio.enums.StatusEmail;
import com.example.desafio.models.LogModel;
import com.example.desafio.models.MailModel;
import com.example.desafio.models.PersonModel;
import com.example.desafio.repositories.LogRepository;
import com.example.desafio.services.LogService;
import com.example.desafio.services.MailService;
import com.example.desafio.services.PersonService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    private final LogRepository logRepository;

    private final PersonService personService;

    private final MailService mailService;

    @Autowired
    private JavaMailSender emailSender;

    public LogController(LogService logService, LogRepository logRepository, PersonService personService, MailService mailService) {
        this.logService = logService;
        this.logRepository = logRepository;
        this.personService = personService;
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<Object> saveLog(@RequestBody @Valid LogDto logDto){
        LogModel logModel = new LogModel();
        MailModel mailModel = new MailModel();

        BeanUtils.copyProperties(logDto, logModel);

        Optional<PersonModel> personModelOptional = personService.findById(logDto.getPersonId());

        logModel.setArriveTime(LocalDateTime.now());
        logModel.setPersonModel(personModelOptional.get());

        if(personModelOptional.isPresent()) {
            try {
                mailModel.setEmailFrom("padilhaklaus@gmail.com");
                mailModel.setEmailTo(personModelOptional.get().getEmail());
                mailModel.setSendDateEmail(logModel.getArriveTime());
                mailModel.setSubject("Movimentação na Mansão");
                mailModel.setText("Saudações, Sr(a) " + personModelOptional.get().getName() + "! Foi registrado sua entrada na Mansão para Jovens Superdotados, na Data: " + logModel.getArriveTime());

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(mailModel.getEmailFrom());
                message.setTo(mailModel.getEmailTo());
                message.setSubject(mailModel.getSubject());
                message.setText(mailModel.getText());
                emailSender.send(message);

                mailModel.setStatusEmail(StatusEmail.SENT);

                if(!personModelOptional.get().getSpecie().equalsIgnoreCase("homo superior")){
                    SimpleMailMessage messageAlert = new SimpleMailMessage();
                    messageAlert.setFrom(mailModel.getEmailFrom());
                    messageAlert.setTo("profCharlesXavier@gmail.com");
                    messageAlert.setSubject("Não mutante na Mansão!");
                    messageAlert.setText("Caro Prof. Xavier, foi registrada a movimentação de um não mutante na Mansão!");
                    emailSender.send(messageAlert);
                }

            } catch (MailException e) {
                mailModel.setStatusEmail(StatusEmail.ERROR);
            } finally {
                mailService.save(mailModel);
                return ResponseEntity.status(HttpStatus.CREATED).body(logService.save(logModel));
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person not founded!");
        }
    }

    @GetMapping
    public ResponseEntity<List<LogModel>> getAllLogs(){
        return ResponseEntity.status(HttpStatus.FOUND).body(logRepository.findAll());
    }

    @GetMapping("/logData")
    public ResponseEntity<Object> Loglist(){

        List<LogModel> logModelOptional = logRepository.findAll();

        StringBuilder sb = new StringBuilder();

        for(LogModel logModel : logModelOptional){
            sb.append(logModel.toString());
            sb.append(System.lineSeparator());
        }
        String text = sb.toString();

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("padilhaklaus@gmail.com");
            message.setTo("profCharlesXavier@gmail.com");
            message.setSubject("Log List");
            message.setText(text);

            emailSender.send(message);

        } catch (MailException e) {
        }

        return ResponseEntity.status(HttpStatus.OK).body("Email sent!");
    }

    @PutMapping("/logmov/{id}")
    public ResponseEntity<Object> editLogById(@PathVariable(value = "id") Long id, @RequestBody @Valid LogDto logDto){
        Optional<LogModel> logModelOptional = logRepository.findById(id);

        if(!logModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Log not founded!");
        } else {
            LogModel logModel = logModelOptional.get();

            logModel.setArriveTime(logModelOptional.get().getArriveTime());
            logModel.setLeaveTime(LocalDateTime.now());
            logModel.setStatus(logDto.getStatus());

            return ResponseEntity.status(HttpStatus.OK).body(logService.save(logModel));

        }
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Object> deleteLog(@PathVariable(value = "id") Long id){
        Optional<LogModel> logModelOptional = logRepository.findById(id);

        if(!logModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Couldn't find the log!");
        } else {
            logService.delete(logModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Log " + id + " successfully deleted!");
        }

    }

}
