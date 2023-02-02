package com.example.desafio.repositories;

import com.example.desafio.models.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Long> {
    @Query("select p from PersonModel p where upper(p.powerLevel) = upper(?1)")
    List<PersonModel> findByPowerLevel(@NonNull String powerLevel);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
