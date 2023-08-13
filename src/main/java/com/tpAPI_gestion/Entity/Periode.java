package com.tpAPI_gestion.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
 public class Periode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "non valide")
    private String description;

    @OneToMany(mappedBy = "periode", cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("category")
   private List<Depenses> depensesAssocier;
}
