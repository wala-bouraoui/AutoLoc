package tn.esprit.tpautoloc.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;

    private LocalDate dateFin;
    @ManyToOne
    @JsonBackReference("reservation-vehicules")
    private Vehicule vehicule;
    @ManyToOne
    @JsonBackReference("reservation-clients")
    private Client client;
    @OneToOne
    private Contrat contrat;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;
}