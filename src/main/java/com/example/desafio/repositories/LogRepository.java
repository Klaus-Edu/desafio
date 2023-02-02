package com.example.desafio.repositories;

import com.example.desafio.models.LogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogModel, Long> {
}
