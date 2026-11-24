package tn.esprit.tpautoloc.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private CategorieVehicule categorie;
    private BigDecimal tarifJournalier;
    private StatutVehicule statut;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="vehicule")
    @JsonManagedReference("maintenance-vehicules")
    private Set<Maintenance> maintenances;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="vehicule")
    @JsonManagedReference("reservation-vehicules")
    private Set<Reservation> reservations;
    @ManyToMany(mappedBy="vehicules", cascade = CascadeType.ALL)
    private Set<Equipement> equipements;
    @ManyToOne
    @JsonBackReference("agence-vehicules")
    private Agence agence;
}
