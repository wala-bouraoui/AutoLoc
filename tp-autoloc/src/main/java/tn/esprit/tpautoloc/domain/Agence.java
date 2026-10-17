package tn.esprit.tpautoloc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;

    private String ville;

    private String adresse;

    private String telephone;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="agence")
    private Set<Employe> Employes;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="agence")
    private Set<Vehicule> Vehicules;
}