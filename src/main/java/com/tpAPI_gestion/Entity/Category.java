package com.tpAPI_gestion.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "champ obligatoire")
    private String type;

    @ManyToOne
    @NotNull(message = "le champ est obligatoire")
    @JoinColumn(name = "idUser",nullable = false)
    private User user;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Budget> budgetsAssoier;



}
