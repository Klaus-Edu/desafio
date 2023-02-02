package com.example.desafio.controllers;

import com.example.desafio.models.MailModel;
import com.example.desafio.services.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/mailbox")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping
    public ResponseEntity<List<MailModel>> getAllMails(){
        return ResponseEntity.status(HttpStatus.FOUND).body(mailService.findAll());
    }
}
