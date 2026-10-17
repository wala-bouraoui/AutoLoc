package tn.esprit.tpautoloc.domain;

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
    Vehicule vehicule;
    @ManyToOne
    Client client;
    @OneToOne
    private Contrat contrat;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;
}