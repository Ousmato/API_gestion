package com.tpAPI_gestion.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(nullable = false)
    private LocalDate starDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Max(value = 500000,message = "le montant ne doit pas depassé 500 000 FCFA")
    @Min(value = 5000, message = "le montant ne pas etre inferieur a 5000 FCFA")
    private int amount;

    @NotNull(message = "champs obligatoire")
    @Column(nullable = false)
    private int seuil;

    @Column(nullable = false)
    private int reliquat;

    @Column(nullable = false)
    private String mois;

    @ManyToOne
    @JoinColumn(name = "idUser",nullable = false)
    @JsonIgnoreProperties(value = {"categoriesCreer","budgetsCreer","depensesEffectuee"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "idCategory",nullable = false)
    @JsonIgnoreProperties(value = {"budgetsAssoier","user"})
    private Category category;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notifications> notificationsAssocier;

    @OneToMany(mappedBy = "budget",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Depenses> depensesSoustraire;

}
