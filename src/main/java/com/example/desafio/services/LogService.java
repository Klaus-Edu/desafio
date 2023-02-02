package com.example.desafio.services;

import com.example.desafio.models.LogModel;
import com.example.desafio.repositories.LogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional
    public LogModel save(LogModel logModel) {
        return logRepository.save(logModel);
    }

    @Transactional
    public void delete(LogModel logModel) {
        logRepository.delete(logModel);
    }
}
