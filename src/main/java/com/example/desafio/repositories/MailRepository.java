package com.example.desafio.repositories;

import com.example.desafio.models.MailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<MailModel, Long> {
}
