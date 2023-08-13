package com.tpAPI_gestion.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String text;

    @Column
    private LocalTime heure;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "idBudget")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

}
