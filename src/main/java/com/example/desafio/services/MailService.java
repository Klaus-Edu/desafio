package com.example.desafio.services;

import com.example.desafio.models.MailModel;
import com.example.desafio.repositories.MailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {
    MailRepository mailRepository;

    @Autowired
    private JavaMailSender emailSender;

    public MailService(MailRepository mailRepository){
        this.mailRepository = mailRepository;
    }

    public List<MailModel> findAll() {
        return mailRepository.findAll();
    }

    @Transactional
    public MailModel save(MailModel mailModel) {
        return  mailRepository.save(mailModel);
    }
}
