package com.tpAPI_gestion.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table
public class ArchivesDepense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "le champ est obligatoire")
    private LocalDate date;

    @Size(min = 10, max = 50,message = "le champ doit avoir au moins 10 caractere")
    private String libelle;

    @NotNull(message = "le champ est obligatoire")
    private int amount;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Depenses depenses;
}
