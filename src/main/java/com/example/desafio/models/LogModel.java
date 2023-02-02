package com.example.desafio.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "TB_LOG")
public class LogModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime arriveTime;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime leaveTime;

    private String status;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private PersonModel personModel;

    @Override
    public String toString() {
        return "Log{" +
                "ID: '" + getId() + '\'' +
                ", Name: '" + getPersonModel().getName() + '\'' +
                ", Arrive time: '" + getArriveTime() + '\'' +
                ", Leave time: '" + getLeaveTime() + '\'' +
                ", Status: '" + getStatus() + '\'' +
                '}';
    }
}
