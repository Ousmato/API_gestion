package com.tpAPI_gestion.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Depenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "le champ est obligatoire")
    private LocalDate date;

    @Size(min = 10, max = 50,message = "le champ doit avoir au moins 10 caractere")
    private String libelle;

    @NotNull(message = "le champ est obligatoire")
    private int amount;

    @ManyToOne
    @JoinColumn(name = "budget", nullable = false)
    @JsonIgnoreProperties(value = {"category","notificationsAssocier","depensesSoustraire"})
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "idPeriode", nullable = false)
    @JsonIgnoreProperties("depensesAssocier")
    private Periode periode;

    @ManyToOne
    @NotNull(message = "le champ est obligatoire")
    @JoinColumn(name = "idUser",nullable = false)
    @JsonIgnoreProperties(value = {"categoriesCreer","budgetsCreer","notificationsAssocier","depensesEffectuee"})
    private User user;

    @Column(nullable = false)
    private Boolean supprimer = false;

  // @OneToOne(mappedBy = "depenses",orphanRemoval = true)
    //@JoinColumn(name = "idArchive",nullable = false)
   // @JsonIgnoreProperties(value = {"user","periode","budget"})
   // private ArchivesDepense archive;
}
